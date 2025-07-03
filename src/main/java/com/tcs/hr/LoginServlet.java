package com.tcs.hr;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.Date;

/**
 * Handles user login and issues a JWT that the client must send back in the
 * <code>Authorization: Bearer &lt;token&gt;</code> header for every protected
 * request. <p> The signing key is loaded from the {@code JWT_SECRET}
 * environment variable. If that variable is absent, the servlet generates a
 * random 256‑bit key on first start‑up and persists it in a file named
 * <code>jwt-secret.key</code> inside the Tomcat base directory so that all
 * subsequent redeploys use the exact same key.</p>
 */
@WebServlet(name = "login", urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String GEOCODING_API_URL =
            "https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=AIzaSyAupWVhWnazaOJp85AYnCHxIMOzmmFg_D4";

    /* ---------------------------------------------------------------------
     *  JWT key handling
     * ------------------------------------------------------------------- */
    public static final Key SECRET_KEY = loadOrCreateKey();

    private static Key loadOrCreateKey() {

    	String env = System.getenv("JWT_SECRET");
        if (env != null && !env.trim().isEmpty()) {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(env.trim()));
        }

        /* 2️⃣ Try file inside $CATALINA_BASE */
        Path keyFile = Paths.get(System.getProperty("catalina.base", "."), "jwt-secret.key");
        try {
            if (Files.exists(keyFile)) {
                String stored = Files.readString(keyFile, StandardCharsets.UTF_8).trim();
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(stored));
            }
        } 
        catch (IOException ignored) {
            // fall through to generate a new key
        }

        /* 3️⃣ Generate and persist a new key */
        byte[] random = new byte[32]; 
        new SecureRandom().nextBytes(random);
        String base64 = Base64.getEncoder().encodeToString(random);
        try {
            Files.writeString(keyFile, base64, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to save generated JWT key to " + keyFile, ex);
        }
        return Keys.hmacShaKeyFor(random);
    }

    /* --------------------------------------------------------------------- */

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username  = request.getParameter("username");
        String password  = request.getParameter("password");
        String latitude  = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");

        boolean isApiRequest = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("login"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject json = new JSONObject();

        /* ----------------------------------------------------------------- */
        // Optional reverse‑geocoding
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
                // Fail soft — we still allow login
                e.printStackTrace();
            }
        }

        /* ----------------------------------------------------------------- */
        // Authenticate against DB
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new ServletException("MySQL driver missing", ex);
        }

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
             PreparedStatement ps = con.prepareStatement(
                     "SELECT id, empId, role FROM users WHERE username=? AND password=?")) {

            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // ✅ Credentials ok
                    int    userId = rs.getInt("id");
                    String empId  = rs.getString("empId");
                    String role   = rs.getString("role");

                    HttpSession session = request.getSession(true);
                    session.setAttribute("user", username);
                    session.setAttribute("role", role);
                    session.setAttribute("empid", empId);
                    session.setAttribute("latitude", latitude);
                    session.setAttribute("longitude", longitude);
                    session.setAttribute("location", location);
                    session.setAttribute("userId", userId);

                    // 🔑  Issue JWT, valid 24 h
                    long now  = System.currentTimeMillis();
                    String jwt = Jwts.builder()
                            .setSubject(username)
                            .claim("role", role)
                            .claim("empid", empId)
                            .claim("userid", userId)
                            .setIssuedAt(new Date(now))
                            .setExpiration(new Date(now + 24 * 60 * 60 * 1000))
                            .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                            .compact();

                    session.setAttribute("token", jwt);

                    json.put("status", "success")
                        .put("message", "Login successful")
                        .put("user", username)
                        .put("role", role)
                        .put("empid", empId)
                        .put("latitude", latitude)
                        .put("longitude", longitude)
                        .put("location", location)
                        .put("token", jwt)
                        .put("userId", userId);

                    if (isApiRequest) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(json.toString());
                    } else {
                        // Redirect according to role
                        switch (role.toLowerCase()) {
                            case "hr":          response.sendRedirect("hr"); break;
                            case "admin":       response.sendRedirect("adminpanel.jsp"); break;
                            case "manager":     response.sendRedirect("manager.jsp"); break;
                            case "salesperson": response.sendRedirect("salesdashboard.jsp"); break;
                            case "salesmanager":response.sendRedirect("salesmanager.jsp"); break;
                            case "accountant":  response.sendRedirect("AccountantDashboard.jsp"); break;
                            default:             response.sendRedirect("dashboard.jsp");
                        }
                    }
                    return;
                }
            }

            // ❌ Bad credentials
            json.put("status", "error")
                .put("message", "Invalid credentials");
            if (isApiRequest) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(json.toString());
            } else {
                response.sendRedirect("login.jsp?error=Invalid%20credentials");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            json.put("status", "error")
                .put("message", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(json.toString());
        }
    }
}
