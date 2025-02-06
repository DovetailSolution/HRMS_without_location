package com.tcs.hr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/updateTaskStatus")
public class updateTaskStatus extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public updateTaskStatus() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Get the current employee's ID from the session
            HttpSession session = request.getSession();
            String empid = (String) session.getAttribute("empid");

            // Get task ID and new status from the request parameters
            String taskId = request.getParameter("taskId");
            String status = request.getParameter("status");

            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            // Prepare and execute the SQL statement to update the task status
            String sql = "UPDATE tasks SET status = ? WHERE task_id = ? AND assigned_to = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, taskId);
            ps.setString(3, empid);

           ps.executeUpdate();

           response.sendRedirect("dashboard.jsp");
           
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
    }
}
