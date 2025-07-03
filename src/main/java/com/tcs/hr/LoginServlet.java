package com.tcs.hr;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.Key;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;
import java.util.Date;

@WebServlet(name = "login", urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /* ---------------------------------------------------------------------
     * Reverse‑geocoding (optional)
     * ------------------------------------------------------------------- */
    private static final String GEOCODING_API_URL =
        "https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=AIzaSyAupWVhWnazaOJp85AYnCHxIMOzmmFg_D4";

    /* ---------------------------------------------------------------------
     * JWT key handling  — NO Keys/Decoders in 0.9.1
     * ------------------------------------------------------------------- */
    public static final Key SECRET_KEY = loadOrCreateKey();

    private static Key loadOrCreateKey() {
        // 1️⃣ Environment variable
        String env = System.getenv("JWT_SECRET");   // must be Base64
        if (env != null && !env.trim().isEmpty()) {
            return keyFromBase64(env.trim());
        }

        // 2️⃣ File under $CATALINA_BASE
        Path keyFile = Paths.get(System.getProperty("catalina.base", "."), "jwt-secret.key");
        try {
            if (Files.exists(keyFile)) {
                String stored = Files.readString(keyFile, StandardCharsets.UTF_8).trim();
                return keyFromBase64(stored);
            }
        } catch (IOException ignored) { }

        // 3️⃣ Generate new 256‑bit key and persist
        byte[] random = new byte[32];
        new SecureRandom().nextBytes(random);
        String base64 = Base64.getEncoder().encodeToString(random);
        try {
            Files.writeString(keyFile, base64, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to save generated JWT key to " + keyFile, ex);
        }
        return new SecretKeySpec(random, SignatureAlgorithm.HS256.getJcaName());
    }

    private static Key keyFromBase64(String b64) {
        byte[] decoded = Base64.getDecoder().decode(b64);
        return new SecretKeySpec(decoded, SignatureAlgorithm.HS256.getJcaName());
    }

    /* --------------------------------------------------------------------- */

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username  = request.getParameter("username");
        String password  = request.getParameter("password");
        String latitude  = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");

        boolean isApiRequest = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject json = new JSONObject();

        /* ---------------- optional reverse‑geocoding ---------------- */
        String location = "Unknown";
        if (latitude != null && longitude != null) {
            try {
                String urlStr = String.format(GEOCODING_API_URL, latitude, longitude);
                HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
                conn.setRequestMethod("GET");
                try (InputStreamReader in = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)) {
                    JSONObject geo = new JSONObject(new JSONTokener(in));
                    JSONArray results = geo.optJSONArray("results");
                    if (results != null && !results.isEmpty()) {
                        location = results.getJSONObject(0).optString("formatted_address", location);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();   // fail soft
            }
        }

        /* ---------------- authenticate ---------------- */
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException ex) { throw new ServletException("MySQL driver missing", ex); }

        try (Connection con = DriverManager.getConnection(
                 "jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
             PreparedStatement ps = con.prepareStatement(
                 "SELECT id, empId, role FROM users WHERE username=? AND password=?")) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    /* ✅ credentials OK */
                    int    userId = rs.getInt("id");
                    String empId  = rs.getString("empId");
                    String role   = rs.getString("role");

                    HttpSession session = request.getSession(true);
                    session.setAttribute("user",      username);
                    session.setAttribute("role",      role);
                    session.setAttribute("empid",     empId);
                    session.setAttribute("latitude",  latitude);
                    session.setAttribute("longitude", longitude);
                    session.setAttribute("location",  location);
                    session.setAttribute("userId",    userId);

                    /* 🔑 issue JWT (24 h) */
                    long now = System.currentTimeMillis();
                    String jwt = Jwts.builder()
                            .setSubject(username)
                            .claim("role",  role)
                            .claim("empid", empId)
                            .claim("userid", userId)
                            .setIssuedAt(new Date(now))
                            .setExpiration(new Date(now + 86_400_000L))   // 24 h
                            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                            .compact();

                    session.setAttribute("token", jwt);

                    json.put("status",   "success")
                        .put("message",  "Login successful")
                        .put("user",     username)
                        .put("role",     role)
                        .put("empid",    empId)
                        .put("latitude", latitude)
                        .put("longitude",longitude)
                        .put("location", location)
                        .put("token",    jwt)
                        .put("userId",   userId);

                    if (isApiRequest) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(json.toString());
                    } else {
                        switch (role.toLowerCase()) {
                            case "hr":           response.sendRedirect("hr");                break;
                            case "admin":        response.sendRedirect("adminpanel.jsp");    break;
                            case "manager":      response.sendRedirect("manager.jsp");       break;
                            case "salesperson":  response.sendRedirect("salesdashboard.jsp");break;
                            case "salesmanager": response.sendRedirect("salesmanager.jsp");  break;
                            case "accountant":   response.sendRedirect("AccountantDashboard.jsp"); break;
                            default:             response.sendRedirect("dashboard.jsp");
                        }
                    }
                    return;
                }
            }

            /* ❌ bad credentials */
            json.put("status", "error")
                .put("message", "Invalid credentials");
            if (isApiRequest) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(json.toString());
            } else {
                response.sendRedirect("login.jsp?error=Invalid%20credentials");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            json.put("status", "error")
                .put("message", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(json.toString());
        }
    }
}
