package com.tcs.hr;

import java.io.IOException;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@WebServlet(name = "login", urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {
	
	
private static final String GEOCODING_API_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=AIzaSyAupWVhWnazaOJp85AYnCHxIMOzmmFg_D4";
public static final Key SECRET_KEY = generateKey();

private static Key generateKey() {
    byte[] keyBytes = new byte[32]; 			// 256 bits for HS256
    new SecureRandom().nextBytes(keyBytes);
    return Keys.hmacShaKeyFor(keyBytes);
}

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String latitude = request.getParameter("latitude");
    String longitude = request.getParameter("longitude");
    String location = "Unknown Location";    

    boolean isApiRequest = "XMLHttpRequest".equals(request.getHeader("login"));

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    JSONObject jsonResponse = new JSONObject();

    if (latitude != null && longitude != null) {
        try {
            String urlStr = String.format(GEOCODING_API_URL, latitude, longitude);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            JSONObject json = new JSONObject(new JSONTokener(in));
            JSONArray results = json.getJSONArray("results");
            if (results.length() > 0) {
                JSONObject addressComponents = results.getJSONObject(0);
                location = addressComponents.getString("formatted_address");
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

        PreparedStatement ps = con.prepareStatement("SELECT id, empId, role FROM users WHERE username=? AND password=?");
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int userId = rs.getInt("id");
            String empId = rs.getString("empId");
            String role = rs.getString("role");

            HttpSession session = request.getSession();
            session.setAttribute("user", username);
            session.setAttribute("role", role);
            session.setAttribute("empid", empId);
            session.setAttribute("latitude", latitude); 
            session.setAttribute("longitude", longitude);   
            session.setAttribute("location", location);       
            session.setAttribute("userId", userId);        
            
            String token = Jwts.builder()            //to create a token 
                .subject(username)
                .claim("role", role)
                .claim("empid", empId)
                .claim("userid", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))  // 1 day expiration
                .signWith(SECRET_KEY)  			// Default algorithm (HS256)
                .compact();

            session.setAttribute("token", token);   //set the token in the session
            System.out.println(token);
            
            if (isApiRequest) {
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "login successfully");
                jsonResponse.put("user", username);
                jsonResponse.put("role", role);
                jsonResponse.put("empid", empId);
                jsonResponse.put("latitude", latitude);
                jsonResponse.put("longitude", longitude);
                jsonResponse.put("location", location);
                jsonResponse.put("token", token);
                jsonResponse.put("userId", userId);   

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(jsonResponse.toString());
            }  
            else {
            	if ("hr".equalsIgnoreCase(role)) {
            	    response.sendRedirect("hr");
            	} 
            	else if("admin".equalsIgnoreCase(role)) {
            	    response.sendRedirect("adminpanel.jsp");
            	} 
            	else if("manager".equals(role))
            	{
            		response.sendRedirect("manager.jsp");
            	}
            	else if("salesperson".equals(role))
            	{
            		response.sendRedirect("salesdashboard.jsp");
            	}
            	else if("salesmanager".equals(role))
            	{
            		response.sendRedirect("salesmanager.jsp");
            	}
            	else if("accountant".equals(role))
            	{
            		response.sendRedirect("AccountantDashboard.jsp");
            	}
            	else 
            	{
            	    response.sendRedirect("dashboard.jsp");
            	}
            }
        } 
        else {
            if (isApiRequest) {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Invalid credentials");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(jsonResponse.toString());
            } 
            else {
                response.sendRedirect("login.jsp?error=Invalid credentials");
            }
        }
    } 
    catch (Exception e) {
        e.printStackTrace();
        jsonResponse.put("status", "error");
        jsonResponse.put("message", "An error occurred: " + e.getMessage());
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(jsonResponse.toString());
        
    }
}
/*
 * public static void termination() { try {
 * Class.forName("com.mysql.cj.jdbc.Driver"); Connection
 * con=DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db",
 * "root","manager");
 * 
 * PreparedStatement ps=con.prepareStatement("")
 * 
 * } catch (Exception e) { // TODO: handle exception } }
 */
}
