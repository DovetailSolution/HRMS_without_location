package com.tcs.hr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "leave", urlPatterns = { "/leave" })
public class LeaveServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	HttpSession session = request.getSession();
    	//String role=(String) session.getAttribute("role");
    	
    	//Integer userId=(Integer) session.getAttribute("userId");
    	
    	/*if (session == null || userId == null) {
    	    response.sendRedirect("login.jsp");
    	    return;
    	}*/
        
        String leaveType = request.getParameter("leaveType");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String action = request.getParameter("action");
        String leaveRemarks=request.getParameter("leaveRemarks");
        //String empid = (String) session.getAttribute("empid");
      //String requester = (String) session.getAttribute("user");

        boolean isApiRequest = "XMLHttpRequest".equals(request.getHeader("leave"));
        
        String authHeader=request.getHeader("Authorization");          //extract token from header
        String token=null;
        
        if(authHeader!=null && authHeader.startsWith("Bearer "))
        {
        	token=authHeader.substring(7);
        	System.out.println(token);
        }
        
        if(token==null)
        {
        	token=(String) session.getAttribute("token");
        }
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();

        if (token == null || token.isEmpty()) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Missing token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(jsonResponse.toString());
            return;
        }
        
        Map<String, Object> claims = claim(token);
        	String role=(String) claims.get("role");
        	Integer userId=(Integer) claims.get("userid");
        	String requester=(String) claims.get("username");
        	String empid=(String) claims.get("empId");
        	
        	System.out.println(userId);
        	System.out.println(requester);
        	System.out.println(empid);
        	
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            if ("submit".equals(action)) {
            	
                if (userId == null) {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "userId is null");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write(jsonResponse.toString());
                    return;
                }
                
                LocalDate startDate = LocalDate.parse(startDateStr);
                LocalDate endDate = LocalDate.parse(endDateStr);
                
                System.out.println(startDate);
                
                // Calculate days taken
                int daysTaken = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
                
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO leave_requests (user_id, empId, leave_type, start_date, end_date, leave_remarks, requester, status, days_taken) VALUES (?, ?, ?, ?, ?, ?, ?, 'Pending', ?)"
                );
                ps.setInt(1, userId);
                ps.setString(2, empid);
                ps.setString(3, leaveType);
                ps.setDate(4, java.sql.Date.valueOf(startDate));
                ps.setDate(5, java.sql.Date.valueOf(endDate));
                ps.setString(6, leaveRemarks);
                ps.setString(7, requester);
                ps.setInt(8, daysTaken);
                
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                	
                	System.out.println("inserted");
                    if (isApiRequest) {
                        jsonResponse.put("status", "success");
                        jsonResponse.put("message", "Leave request submitted successfully");
                        jsonResponse.put("userid", userId);
                        jsonResponse.put("empid", empid);
                        jsonResponse.put("leaveType", leaveType);
                        jsonResponse.put("startDate", startDateStr);
                        jsonResponse.put("endDate", endDateStr);
                        jsonResponse.put("Remarks", leaveRemarks);
                        jsonResponse.put("requester", requester);
                        jsonResponse.put("daysTaken", daysTaken);
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(jsonResponse.toString());
                    } 
                    else 
                    {
                        response.sendRedirect("dashboard.jsp?message=Leave request submitted successfully");
                    }
                } 
                else 
                {
                    if (isApiRequest) {
                        jsonResponse.put("status", "error");
                        jsonResponse.put("message", "Failed to submit leave request");
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.getWriter().flush();
                    }
                    else 
                    {
                        response.sendRedirect("dashboard.jsp?error=Failed to submit leave request");
                    }
                }
                con.close();
            } 
            else if ("status".equals(action)) {
                PreparedStatement pc = con.prepareStatement(
                    "SELECT u.empId, lr.user_id, lr.leave_type, lr.start_date, lr.end_date, lr.requester, lr.status, lr.leave_payment_type, lr.days_taken " +
                    "FROM users u JOIN leave_requests lr ON u.empId = lr.empId WHERE u.empId = ?"
                );
                pc.setString(1, empid);
                ResultSet rs = pc.executeQuery();

                List<AttendanceRecord> leaveRequests = new ArrayList<>();    //to declare an array list 
                while (rs.next()) 
                {
                    AttendanceRecord leaveRequest = new AttendanceRecord();
                    leaveRequest.setEmpId(rs.getString("empId"));
                    leaveRequest.setUserid(rs.getInt("user_id"));
                    leaveRequest.setLeaveType(rs.getString("leave_type"));
                    leaveRequest.setStartdate(rs.getDate("start_date"));
                    leaveRequest.setEnddate(rs.getDate("end_date"));
                    leaveRequest.setRequester(rs.getString("requester"));
                    leaveRequest.setStatus(rs.getString("status"));
                    leaveRequest.setLeavePaymentType(rs.getString("leave_payment_type"));
                    leaveRequest.setDaysTaken(rs.getInt("days_taken"));
                    leaveRequests.add(leaveRequest);
                }
                
                if (isApiRequest) {
                    JSONArray jsonLeaveArray = new JSONArray();
                    
                    for (AttendanceRecord record : leaveRequests) 
                    {
                        JSONObject arrayResponse = new JSONObject();
                        arrayResponse.put("empId", record.getEmpId());
                        arrayResponse.put("userId", record.getUserid());
                        arrayResponse.put("leaveType", record.getLeaveType());
                        arrayResponse.put("startDate", record.getStartdate().toString());
                        arrayResponse.put("endDate", record.getEnddate().toString());
                        arrayResponse.put("requester", record.getRequester());
                        arrayResponse.put("status", record.getStatus());
                        arrayResponse.put("leavePaymentType", record.getLeavePaymentType());
                        arrayResponse.put("daysTaken", record.getDaysTaken());
                        jsonLeaveArray.put(arrayResponse);
                    }
                    
                    // Wrap the array inside a response object
                    //JSONObject jsonResponse1 = new JSONObject();
                    jsonResponse.put("status", "success");
                    jsonResponse.put("leaveRequests", jsonLeaveArray);
                    
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(jsonResponse.toString());
                }

                	else {
                    request.setAttribute("leaveRequests", leaveRequests);
                    request.getRequestDispatcher("leavestatus.jsp").forward(request, response);
                }
            }
            con.close();
        } 
        
        catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "An error occurred: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(jsonResponse.toString());
        }
    }

    /*protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	HttpSession session = request.getSession();
        //String role = (String) session.getAttribute("role");
    	
    	String authHeader=request.getHeader("Authorization");
    	String token=null;
    	
    	if(authHeader!=null && authHeader.startsWith("Bearer "))
    	{
    		token=authHeader.substring(7);
    	}
    	
    	if(token==null)
    	{
    		token=(String) session.getAttribute("token");
    	}
    	
    	Map<String, Object> claims=claim(token);
    	String role=(String) claims.get("role");
    	
    	
        if (!"hr".equals(role)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String leaveId = request.getParameter("leaveId");
        String decision = request.getParameter("decision");
        boolean isApiRequest = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();

        if (isApiRequest) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Invalid or missing token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        if (leaveId != null && decision != null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

                PreparedStatement ps = con.prepareStatement("UPDATE leave_requests SET status = ? WHERE id = ?");
                ps.setString(1, decision);
                ps.setInt(2, Integer.parseInt(leaveId));

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    if (isApiRequest) {
                        jsonResponse.put("status", "success");
                        jsonResponse.put("message", "Leave request " + decision.toLowerCase() + " successfully");
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(jsonResponse.toString());
                    } else {
                        response.sendRedirect("hr?message=Leave request " + decision.toLowerCase() + " successfully");
                    }
                } else {
                    if (isApiRequest) {
                        jsonResponse.put("status", "error");
                        jsonResponse.put("message", "Failed to process leave request");
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.getWriter().write(jsonResponse.toString());
                    } else {
                        response.sendRedirect("hr?error=Failed to process leave request");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "An error occurred: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(jsonResponse.toString());
            }
        }
}*/
//------------------------------------------------------------------------------------------------------    	
    private Map<String, Object> claim(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(LoginServlet.SECRET_KEY) // Use the shared SECRET_KEY
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject(); // Extract the "sub" claim
        String empId = claims.get("empid", String.class); // Extract the "empId" claim
        String role = claims.get("role", String.class);
        
        // Handling userid properly
        Integer userid = null;
        if (claims.get("userid") != null) {
            userid = ((Number) claims.get("userid")).intValue();
        }

        Map<String, Object> claimMap = new HashMap<>();
        claimMap.put("username", username);
        claimMap.put("empId", empId);
        claimMap.put("role", role);
        claimMap.put("userid", userid); // Now it's allowed

        return claimMap;
    }
}
