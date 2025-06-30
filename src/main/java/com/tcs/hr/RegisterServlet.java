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
import javax.servlet.http.HttpSession;

@WebServlet(name = "register", urlPatterns = { "/register" })
	
	public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String empId=request.getParameter("empId");
        String gender=request.getParameter("gender");
        String dob=request.getParameter("DOB");
        String email=request.getParameter("email");
        String mobile=request.getParameter("mobile");
        String nationality=request.getParameter("nationality");
        String blood=request.getParameter("blood");
        String manager=request.getParameter("managerEmpId");
        
        
        HttpSession session = request.getSession();
        session.setAttribute("empid", empId);
        session.setAttribute("username", username);
        session.setAttribute("managerId", manager);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            // Check if user already exists
            PreparedStatement checkUser = con.prepareStatement("SELECT * FROM users WHERE empId = ?");
            checkUser.setString(1, empId);
            ResultSet rs = checkUser.executeQuery();

            if (rs.next()) 
            {
                // User already exists, redirect to login page with an error message
                response.sendRedirect("login.jsp?error=User already registered. Please log in.");
            } 
            
            else {
                // Register the new user
                PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password, role, empId, gender, dob, email, mobile, nationality, blood, reporting_manager) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, role);
                ps.setString(4, empId);
                ps.setString(5, gender);
                ps.setString(6, dob);
                ps.setString(7, email);
                ps.setString(8, mobile);
                ps.setString(9, nationality);
                ps.setString(10, blood);
                ps.setString(11, manager);
                
                ps.executeUpdate();

                response.sendRedirect("family.jsp?success=Registration successful. Please log in.");
            
            }
            
            con.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("register.jsp?error=An error occurred. Please try again.");
        }
    }
}

