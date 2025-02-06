package com.tcs.hr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Execute;

@WebServlet(name = "reminder", urlPatterns = { "/reminder" })
public class Reminder extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Reminder() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String empid = (String) session.getAttribute("empid");

        if (empid == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User session is invalid. Please log in again.");
            return;
        }

        List<Map<String, String>> reminders = new ArrayList<>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
             PreparedStatement ps = con.prepareStatement("SELECT date, time, event FROM reminder WHERE empid = ?")) {

            ps.setString(1, empid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> reminder = new HashMap<>();
                reminder.put("date", rs.getDate("date").toString());
                reminder.put("time", rs.getTime("time").toString());
                reminder.put("event", rs.getString("event"));
                reminders.add(reminder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("reminders", reminders);
        request.getRequestDispatcher("reminder.jsp").forward(request, response);
    }

    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String event = request.getParameter("event");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("user");
        String empid = (String) session.getAttribute("empid");

        if (username == null || empid == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User session is invalid. Please log in again.");
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish Connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            // Prepare SQL statement
            String sql = "INSERT INTO reminder(username, empid, date, time, event) VALUES (?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, empid);
            ps.setString(3, date);
            ps.setString(4, time);
            ps.setString(5, event);

            // Execute Update
            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                response.getWriter().write("reminder set succssfully");
                response.sendRedirect("reminder.jsp");
            } 
            else 
            {
                response.getWriter().println("Failed to set the reminder.");
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            response.getWriter().println("An error occurred: " + e.getMessage());
        } 
        finally 
        {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
