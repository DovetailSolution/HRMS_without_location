package com.tcs.hr;

import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tcs.hr.LoginServlet;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@WebServlet(name = "attendance", urlPatterns = { "/attendance" })
public class AttendanceServlet extends HttpServlet {

    //private static final String GEOCODING_API_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=AIzaSyChwWhY3MT7WGJkC6IkJSgK5vyUehcjnDY";
    
    //private static final String GEOCODING_API_URL="https://api-bdc.net/data/reverse-geocode?latitude=%s&longitude=%s&localityLanguage=en&key=bdc_387285a4265a47268f553510f766c48f";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/*
	 * private static final Key SECRET_KEY = generateKey();
	 * 
	 * private static Key generateKey() { byte[] keyBytes = new byte[32]; // 256
	 * bits for HS256 new SecureRandom().nextBytes(keyBytes); return
	 * Keys.hmacShaKeyFor(keyBytes); }
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    	HttpSession session = request.getSession();
        //String empid = (String) session.getAttribute("empid");

        boolean isApiRequest = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));    //mention header 
                   //create object of an api
        
        String authHeader =request.getHeader("Authorization");
        String token=null;
        
        if (authHeader != null && authHeader.startsWith("Bearer "))     //extract token from header 
        {
            token = authHeader.substring(7); 							// Remove "Bearer " prefix
        }
        
        if(token==null)
        {
        	 token=(String) session.getAttribute("token");
        }
        
        JSONObject jsonResponse = new JSONObject();
        
        if (token == null || token.isEmpty()) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Missing token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(jsonResponse.toString());
            return;
        }
        
        Map<String, String> claims = claim(token);
    	//String username1=claims.get("username");
    	String empId=claims.get("empId");
        
        if (empId == null) {
            if (isApiRequest) {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "User not logged in.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonResponse.toString());
            } 
            else
            {
                response.sendRedirect("login.jsp");
            }
            return;
        }

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            PreparedStatement ps = con.prepareStatement(
                "SELECT u.username, attendance.* FROM users u JOIN attendance ON u.empId = attendance.empId WHERE u.empId = ?");
            
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();

            JSONArray attendanceRecords = new JSONArray(); 

            while (rs.next()) {
                JSONObject record = new JSONObject();

                String username = rs.getString("username");
                java.sql.Timestamp signInTime = rs.getTimestamp("sign_in_time");
                java.sql.Timestamp signOutTime = rs.getTimestamp("sign_out_time");
                String signInLocation = rs.getString("location");
                String signOutLocation = rs.getString("sign_out_location");

                // Adding details to JSON record
                record.put("username", username);
                record.put("sign_in_time", signInTime != null ? signInTime.toString() : null);
                record.put("sign_out_time", signOutTime != null ? signOutTime.toString() : null);
                record.put("sign_in_location", signInLocation);
                record.put("sign_out_location", signOutLocation);

                // Add the record to the array
                attendanceRecords.put(record);
            }

            // Final JSON response
            if (isApiRequest) {
                jsonResponse.put("status", "success");
                jsonResponse.put("attendance", attendanceRecords);

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonResponse.toString());
                response.getWriter().flush();
            } 
            else {
                // Handle non-AJAX request (e.g., render on JSP page or forward to another servlet)
                request.setAttribute("attendanceRecords", attendanceRecords);
                request.getRequestDispatcher("attendance.jsp").forward(request, response);
            }

            // Close resources
            rs.close();
            ps.close();
            con.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            if (isApiRequest) {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "An error occurred while fetching the data.");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonResponse.toString());
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while fetching the data.");
            }
        }
    }
//------------------------------------------------------------------------------------------------------------  
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        
	        HttpSession session = request.getSession();
	        //String username = (String) session.getAttribute("user");
	        String empid = (String) session.getAttribute("empid");
	        String action = request.getParameter("action");
	        
	        // Determine if the request is an AJAX request
	        boolean isApiRequest = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	        
	        String authHeader =request.getHeader("Authorization");
	        String token=null;
	        
	        if (authHeader != null && authHeader.startsWith("Bearer ")) 
	        {
	            token = authHeader.substring(7); // Remove "Bearer " prefix
	        }

	        if(token==null )
	        {
	        	 token=(String) session.getAttribute("token");
	        }
	
	        JSONObject jsonResponse = new JSONObject();
	//-------------------------------------------------------------------------------------------------
	        //String token1=(String) session.getAttribute("token");
	        System.out.println(token);
	       
	        if (token == null || token.isEmpty()) {
	            jsonResponse.put("status", "error");
	            jsonResponse.put("message", "Missing token");
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write(jsonResponse.toString());
	            return;
	        }
	        
	        Map<String, String> claims = claim(token);
        	String username1=claims.get("username");
        	String empId=claims.get("empId");
        	
        	System.out.println(username1);
        	System.out.println(empId);
        	
	        if (username1 == null) 
	        {
	            if (isApiRequest) {
	                jsonResponse.put("status", "error");
	                jsonResponse.put("message", "User not logged in.");
	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(jsonResponse.toString());
	            } 
	            else
	            {
	                response.sendRedirect("login.jsp");
	            }
	            return;
	        }
	
	 //-----------------------------------------------------------------------------------------------------
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
	
	            if ("signin".equals(action)) 
	            {
	                String latitude = request.getParameter("latitude");
	                String longitude = request.getParameter("longitude");
	
	                String location = fetchlocation(latitude, longitude);
	                ZoneId zoneId = ZoneId.of("Asia/Kolkata");
	                
	                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
	                
	                LocalDateTime SignIn = currentTimestamp.toInstant().atZone(zoneId).toLocalDateTime();

	                // Format the Date-Time
	                String signInTimeIST = DATE_TIME_FORMATTER.format(SignIn);

	                PreparedStatement ps = con.prepareStatement("INSERT INTO attendance (user_id, empId, sign_in_time, location, latitude, longitude) VALUES ((SELECT id FROM users WHERE username = ?), ?, ?, ?, ?, ?)");
	                
	                //PreparedStatement ps = con.prepareStatement("INSERT INTO attendance (user_id, empId, sign_in_time) VALUES ((SELECT id FROM users WHERE username = ?), ?, ?)");
	
	                ps.setString(1, username1);
	                ps.setString(2, empid);
	                ps.setString(3, signInTimeIST);
	                ps.setString(4, location);
	                ps.setString(5, latitude);
	               ps.setString(6, longitude);
	                
	                ps.executeUpdate();
	
	                session.setAttribute("signedIn", true);
	                session.setAttribute("lastSignInTime", signInTimeIST);
	                session.setAttribute("lastSignOutTime", null);
	                session.setAttribute("location", location);
	
	                //String token = (String) session.getAttribute("token");
	
	                if (isApiRequest) 
	                {
	                    jsonResponse.put("status", "success");
	                    jsonResponse.put("message", "Sign in successful.");
	                    jsonResponse.put("signInTime", signInTimeIST);
	                    jsonResponse.put("location", location);
	                    jsonResponse.put("token", token);
	                    
	                    response.setStatus(HttpServletResponse.SC_OK);
	                    response.setContentType("application/json");
	                    response.setCharacterEncoding("UTF-8");
	                    response.getWriter().write(jsonResponse.toString());
	                    response.getWriter().flush(); // Ensure the response is actually sent
	                } 
	                else 
	                {
	                    request.setAttribute("status", "success");
	                    request.setAttribute("message", "Sign in successful.");
	                    request.setAttribute("signInTime", signInTimeIST);
	                    request.setAttribute("location", location);
	                    request.setAttribute("token", token);
	                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
	                }
	            }
	            
	 //------------------------------------------------------------------------------------------------------
	            else if ("signout".equals(action)) 
	            {
	                String latitude = request.getParameter("latitude");
	                String longitude = request.getParameter("longitude");
	
	                String location = fetchlocation(latitude, longitude);
	                ZoneId zoneId = ZoneId.of("Asia/Kolkata");
	
	                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
	                
	                LocalDateTime SignOut = currentTimestamp.toInstant().atZone(zoneId).toLocalDateTime();

	                // Format the Date-Time
	                String signOutTimeIST = DATE_TIME_FORMATTER.format(SignOut);
	
	                PreparedStatement ps = con.prepareStatement("UPDATE attendance SET sign_out_time = ?, sign_out_location = ?, sign_out_latitude = ?, sign_out_longitude = ? WHERE user_id = (SELECT id FROM users WHERE username = ?) AND sign_out_time IS NULL");
	                
	                //PreparedStatement ps = con.prepareStatement("UPDATE attendance SET sign_out_time = ? WHERE user_id = (SELECT id FROM users WHERE username = ?) AND sign_out_time IS NULL");
	
	                ps.setString(1, signOutTimeIST);
	                ps.setString(2, location);
	                ps.setString(3, latitude);
	                ps.setString(4, longitude);
	                ps.setString(5, username1);
	                ps.executeUpdate();
	
	                session.setAttribute("signedIn", false);
	                session.setAttribute("lastSignOutTime", signOutTimeIST);
	                
	                //String token = (String) session.getAttribute("token");
	                
	                if (isApiRequest) 
	                {
	                    jsonResponse.put("status", "success");
	                    jsonResponse.put("message", "Sign out successful.");
	                    jsonResponse.put("signOutTime", signOutTimeIST);
	                    jsonResponse.put("location", location);
	                    jsonResponse.put("token", token);
	                    response.setStatus(HttpServletResponse.SC_OK);
	                    response.setContentType("application/json");
	                    response.setCharacterEncoding("UTF-8");
	                    response.getWriter().write(jsonResponse.toString());
	                    response.getWriter().flush(); // Ensure the response is actually sent
	                } 
	                else 
	                {
	                    request.setAttribute("status", "success");
	                    request.setAttribute("message", "Sign out successful.");
	                    request.setAttribute("signOutTime", signOutTimeIST);
	                    request.setAttribute("location", location);
	                    request.setAttribute("token", token);
	                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
	                }
	            } 
	            
	 //------------------------------------------------------------------------------------------------------
	            else if ("viewstatus".equals(action)) 
	            {
	                System.out.println("view status is called");

	                LocalDate today = LocalDate.now();
	                LocalDateTime startOfDay = today.atStartOfDay();
	                LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
	                Timestamp startTimestamp = Timestamp.valueOf(startOfDay);
	                Timestamp endTimestamp = Timestamp.valueOf(endOfDay);

	                // Fetch ONLY today's records
	                List<AttendanceRecord> dailyRecords = new ArrayList<>();

	                PreparedStatement ps = con.prepareStatement(
	                    "SELECT sign_in_time, sign_out_time, location, sign_out_location " +
	                    "FROM attendance " +
	                    "WHERE user_id = (SELECT id FROM users WHERE username = ?) " +
	                    "AND sign_in_time BETWEEN ? AND ?"
	                );
	                ps.setString(1, username1);
	                ps.setTimestamp(2, startTimestamp);
	                ps.setTimestamp(3, endTimestamp);

	                ResultSet rs = ps.executeQuery();
	                long totalDailyMillis = 0;

	                while (rs.next()) {
	                    AttendanceRecord record = new AttendanceRecord();
	                    record.setSignInTime(rs.getTimestamp("sign_in_time"));
	                    record.setSignOutTime(rs.getTimestamp("sign_out_time"));
	                    record.setLocation(rs.getString("location"));
	                    record.setSignOutLocation(rs.getString("sign_out_location"));

	                    if (record.getSignInTime() != null && record.getSignOutTime() != null) {
	                        totalDailyMillis += record.getSignOutTime().getTime() - record.getSignInTime().getTime();
	                    }

	                    dailyRecords.add(record); // ✅ Store only today's records
	                }

	                // Convert total time worked today into HH:mm:ss format
	                String totalDailyTimeFormatted = formatTime(totalDailyMillis);

	                // Send response (only today's records)
	                if (isApiRequest) {
	                    jsonResponse.put("status", "success");

	                    JSONArray attendanceArray = new JSONArray();    //declaring an array
	                    
	                    for (AttendanceRecord record : dailyRecords) 
	                    {
	                        JSONObject recordJson = new JSONObject();
	                        
	                        recordJson.put("signInTime", record.getSignInTime() != null ? record.getSignInTime().toString() : JSONObject.NULL);
	                        recordJson.put("location", record.getLocation() != null ? record.getLocation() : JSONObject.NULL);
	                        recordJson.put("signOutTime", record.getSignOutTime() != null ? record.getSignOutTime().toString() : JSONObject.NULL);
	                        recordJson.put("signOutLocation", record.getSignOutLocation() != null ? record.getSignOutLocation() : JSONObject.NULL);
	                        attendanceArray.put(recordJson);
	                    }

	                    jsonResponse.put("attendanceRecords", attendanceArray);
	                    jsonResponse.put("totalDailyTime", totalDailyTimeFormatted);
	                    jsonResponse.put("token", token);

	                    response.setStatus(HttpServletResponse.SC_OK);
	                    response.setContentType("application/json");
	                    response.setCharacterEncoding("UTF-8");
	                    response.getWriter().write(jsonResponse.toString());
	                    response.getWriter().flush();
	                    response.getWriter().close();
	                } 
	                else {
	                    request.setAttribute("attendanceRecords", dailyRecords);
	                    request.setAttribute("totalDailyTime", totalDailyTimeFormatted);

	                    request.getRequestDispatcher("status.jsp").forward(request, response);
	                }
	                return;
	            }

	//--------------------------------------------------------------------------------------------------
	            else if ("downloadReport".equals(action)) 
	            {
	                String yearMonth = request.getParameter("yearMonth");

	                if (yearMonth == null || yearMonth.isEmpty()) {
	                    if (isApiRequest) {
	                        jsonResponse.put("status", "error");
	                        jsonResponse.put("message", "yearMonth parameter is missing.");
	                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                        response.setContentType("application/json");
	                        response.setCharacterEncoding("UTF-8");
	                        response.getWriter().write(jsonResponse.toString());
	                    } 
	                    else {
	                        request.setAttribute("status", "error");
	                        request.setAttribute("message", "yearMonth parameter is missing.");
	                        request.getRequestDispatcher("error.jsp").forward(request, response);
	                    }
	                    return;
	                }

	                YearMonth ym = YearMonth.parse(yearMonth);
	                LocalDate startDate = ym.atDay(1);
	                LocalDate endDate = ym.atEndOfMonth();
	                Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
	                Timestamp endTimestamp = Timestamp.valueOf(endDate.atTime(LocalTime.MAX));

	                PreparedStatement ps = null;
	                ResultSet rs = null;

	                try {
	                    ps = con.prepareStatement(
	                        "SELECT sign_in_time, sign_out_time, location, sign_out_location " +
	                        "FROM attendance WHERE user_id = (SELECT id FROM users WHERE username = ?) " +
	                        "AND sign_in_time BETWEEN ? AND ?"
	                    );
	                    ps.setString(1, username1);
	                    ps.setTimestamp(2, startTimestamp);
	                    ps.setTimestamp(3, endTimestamp);
	                    rs = ps.executeQuery();

	                    List<AttendanceRecord> records = new ArrayList<>();
	                    while (rs.next()) {
	                        AttendanceRecord record = new AttendanceRecord();
	                        record.setSignInTime(rs.getTimestamp("sign_in_time"));
	                        record.setSignOutTime(rs.getTimestamp("sign_out_time"));
	                        record.setLocation(rs.getString("location"));
	                        record.setSignOutLocation(rs.getString("sign_out_location"));
	                        records.add(record);
	                    }

	                    // Set response headers for PDF
	                    response.setContentType("application/pdf");
	                    response.setHeader("Content-Disposition", "attachment; filename=attendance_report_" + yearMonth + ".pdf");

	                    // Generate PDF
	                    Document document = new Document();
	                    ServletOutputStream outputStream = response.getOutputStream();
	                    try {
	                        PdfWriter.getInstance(document, outputStream);
	                        document.open();
	                        
	                        // Title
	                        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
	                        Paragraph title = new Paragraph("Attendance Report - " + yearMonth, titleFont);
	                        title.setAlignment(Element.ALIGN_CENTER);
	                        document.add(title);
	                        document.add(new Paragraph("\n"));

	                        // Table
	                        PdfPTable table = new PdfPTable(4);
	                        table.setWidthPercentage(100);
	                        table.addCell("Sign In Time");
	                        table.addCell("Sign Out Time");
	                        table.addCell("Sign In Location");
	                        table.addCell("Sign Out Location");

	                        for (AttendanceRecord record : records) {
	                            String signInTimeStr = record.getSignInTime() != null ? DATE_TIME_FORMATTER.format(record.getSignInTime().toLocalDateTime()) : "";
	                            String signOutTimeStr = record.getSignOutTime() != null ? DATE_TIME_FORMATTER.format(record.getSignOutTime().toLocalDateTime()) : "";
	                            table.addCell(signInTimeStr);
	                            table.addCell(signOutTimeStr);
	                            table.addCell(record.getLocation());
	                            table.addCell(record.getSignOutLocation());
	                        }

	                        document.add(table);
	                    } 
	                    catch (DocumentException e) {
	                        e.printStackTrace();
	                    } 
	                    finally {
	                        if (document.isOpen()) {
	                            document.close(); // Ensure document is closed properly
	                        }
	                        if (outputStream != null) {
	                            outputStream.close();
	                        }
	                    }
	                } 
	                catch (SQLException e) {
	                    e.printStackTrace();
	                } 
	                finally 
	                {
	                    // Close resources 
	                    try {
	                        if (rs != null) rs.close();
	                        if (ps != null) ps.close();
	                    } catch (SQLException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        }
	        catch (Exception e) {
				e.printStackTrace();
			}
	    }

//------------------------------------------------------------------------------------------------------
	    private String formatTime(long millis) {
            long totalSeconds = millis / 1000;
            long hours = totalSeconds / 3600;
            long minutes = (totalSeconds % 3600) / 60;
            long seconds = totalSeconds % 60;
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    
//----------------------------------------------------------------------------------------------------
	    private Map<String, String> claim(String token) {
	        Claims claims = Jwts.parser()
	                .setSigningKey(LoginServlet.SECRET_KEY) // Use the shared SECRET_KEY
	                .build()
	                .parseClaimsJws(token)
	                .getBody();

	        String username = claims.getSubject(); // Extract the "sub" claim
	        String empId = claims.get("empid", String.class); // Extract the "empId" claim

	        Map<String, String> claimMap = new HashMap<>();
	        claimMap.put("username", username);
	        claimMap.put("empId", empId);
	        return claimMap;
	    }

//--------------------------------------------------------------------------------------------------------
	    private String fetchlocation(String latitude, String longitude) {
        String location = "Unknown";

		/*
		 * if (latitude != null && longitude != null) { try { String urlStr =
		 * String.format(GEOCODING_API_URL, latitude, longitude); URL url = new
		 * URL(urlStr); HttpURLConnection conn = (HttpURLConnection)
		 * url.openConnection(); conn.setRequestMethod("GET");
		 * 
		 * System.out.println("Geocoding API URL: " + urlStr);
		 * 
		 * InputStreamReader in = new InputStreamReader(conn.getInputStream());
		 * JSONObject json = new JSONObject(new JSONTokener(in));
		 * 
		 * System.out.println("Geocoding API Response: " + json.toString());
		 * 
		 * JSONArray results = json.getJSONArray("results"); if (results.length() > 0) {
		 * JSONObject addressComponents = results.getJSONObject(0); location =
		 * addressComponents.getString("formatted_address"); }
		 * 
		 * System.out.println("Extracted Location: " + location); } catch (Exception e)
		 * { e.printStackTrace(); } }
		 */

        return location;
    }
//-------------------------------------------------------------------------------------------------------
}
