package com.tcs.hr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "update", urlPatterns = { "/update" })
public class updateEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public updateEmployee() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String empId=request.getParameter("empId");    //from adminpanel.jsp where unique name= empId
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db","root","manager");
			
			PreparedStatement ps=con.prepareStatement("select * from users where empId=?");
			ps.setString(1, empId);
			
			ResultSet rs=ps.executeQuery();
			
			AttendanceRecord record = null;
			while(rs.next())
			{
				record = new AttendanceRecord();
				record.setEmpId(rs.getString("empId"));
				record.setUsername(rs.getString("username"));
				record.setRole(rs.getString("role"));
				record.setGender(rs.getString("gender"));
			    record.setDob(rs.getDate("dob"));
			    record.setEmail(rs.getString("email"));
			    record.setMobile(rs.getInt("mobile"));
			    record.setNationality(rs.getString("nationality"));
			    record.setBlood(rs.getString("blood"));
			}
		request.setAttribute("employees", record);
		request.getRequestDispatcher("editemployee.jsp").forward(request, response);
		}
		catch (Exception e) {

			e.printStackTrace();
		}
		
		}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
		String empId = request.getParameter("empId");
        String username = request.getParameter("username");
        String role = request.getParameter("role");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String nationality = request.getParameter("nationality");
        String blood = request.getParameter("blood");

        try {
        	
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
            
            PreparedStatement ps = con.prepareStatement("UPDATE users SET username = ?, role = ?, gender = ?, dob = ?, email = ?, mobile = ?, nationality = ?, blood = ? WHERE empId = ?");
            ps.setString(1, username);
            ps.setString(2, role);
            ps.setString(3, gender);
            ps.setString(4, dob);
            ps.setString(5, email);
            ps.setString(6, mobile);
            ps.setString(7, nationality);
            ps.setString(8, blood);
            ps.setString(9, empId);
            
            int rowsUpdated = ps.executeUpdate();
            
            if (rowsUpdated > 0) {
                request.setAttribute("message", "Employee updated successfully.");
            } else {
                request.setAttribute("message", "Failed to update employee.");
            }
            
            request.getRequestDispatcher("adminpanel.jsp").forward(request, response);
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

}
