 package com.tcs.hr;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet(name = "manager", urlPatterns = { "/manager" })
@MultipartConfig
public class ManagerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String taskName = request.getParameter("taskName");
        String taskDescription = request.getParameter("taskDescription");
        String assignedTo = request.getParameter("assignedTo");
        String priority = request.getParameter("priority");
        String dueDate = request.getParameter("dueDate");

        // Get manager details from session
        HttpSession session = request.getSession();
        Integer assignedBy = (Integer) session.getAttribute("userId");
        session.setAttribute("assignedTo", assignedTo);

        // Check if the manager is logged in
        if (assignedBy == null) {
            response.sendRedirect("login.jsp?error=Please login as manager");
            return;
        }

        // Handle file upload
        Part filePart = request.getPart("attachment");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String uploadPath = "C:/uploads/" + fileName; // Change path as needed

        try (InputStream fileContent = filePart.getInputStream()) {
            File file = new File(uploadPath);
            file.getParentFile().mkdirs();
            filePart.write(uploadPath);
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String assignedToUsername = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            // Retrieve the username of the assigned employee based on their ID
            String query = "SELECT username FROM users WHERE empId = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, assignedTo);
            rs = ps.executeQuery();

            if (rs.next()) {
                assignedToUsername = rs.getString("username");
            }

            if (assignedToUsername == null) {
                response.sendRedirect("manager.jsp?error=Employee with ID: " + assignedTo + " not found.");
                return;
            }

            // Insert task with assigned employee's username
            String sql = "INSERT INTO tasks (task_name, task_description, assigned_to, username, assigned_by, priority, due_date, attachment_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, taskName);
            ps.setString(2, taskDescription);
            ps.setString(3, assignedTo); // Storing empId
            ps.setString(4, assignedToUsername); // Storing the username
            ps.setInt(5, assignedBy); // Storing manager's ID
            ps.setString(6, priority);
            ps.setDate(7, Date.valueOf(dueDate));
            ps.setString(8, uploadPath);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                response.sendRedirect("manager.jsp?success=Task assigned successfully to: " + assignedToUsername);
            } else {
                response.sendRedirect("manager.jsp?error=Failed to assign task to: " + assignedToUsername);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("manager.jsp?error=An error occurred while assigning the task.");
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
