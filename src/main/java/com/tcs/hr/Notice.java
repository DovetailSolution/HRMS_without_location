package com.tcs.hr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "notice", urlPatterns = { "/notice" })
public class Notice extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Notice() {
        super();
    }

    // GET method to fetch notices
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
                 PreparedStatement ps = con.prepareStatement("SELECT * FROM notice")) {

                ResultSet rs = ps.executeQuery(); // Execute SELECT query

                request.setAttribute("resultSet", rs);

                // Forward to JSP page to display notices
                request.getRequestDispatcher("urgentnotice.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving notices.");
        }
    }

    // POST method to insert a new notice
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String date = request.getParameter("date");
        String message = request.getParameter("message");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
                 PreparedStatement ps = con.prepareStatement("INSERT INTO notice (date, message) VALUES (?, ?)")) {

                ps.setString(1, date);
                ps.setString(2, message);

                int rowsUpdated = ps.executeUpdate();

                if (rowsUpdated > 0) {
                    // Redirect to HR dashboard after successful insertion
                    response.sendRedirect("hr_dashboard.jsp");
                } else {
                    response.getWriter().println("Failed to send notice.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving notice.");
        }
    }
}
