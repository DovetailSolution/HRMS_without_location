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

@WebServlet(name = "resignation", urlPatterns = { "/resignation" })
public class Resignation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Resignation() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String empid = request.getParameter("name");
        Connection con = null;
        PreparedStatement ps = null;

        System.out.println(empid);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
            ps = con.prepareStatement("UPDATE users SET resignation = ? WHERE empId = ?");
            ps.setString(1, "YES"); // Set 'resignation' status to "YES" or relevant value
            ps.setString(2, empid);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                response.getWriter().println("Resignation status updated successfully for user: " + empid);
            } else {
                response.getWriter().println("Failed to update resignation status. User not found: " + empid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("An error occurred: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
