package com.tcs.hr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.mail.SendFailedException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.protocol.ResultBuilder;

@WebServlet(name = "family", urlPatterns = { "/family" })
public class Family extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Family() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session=request.getSession();
		String empid= (String) session.getAttribute("empid");
		String username=(String) session.getAttribute("username");
		
		String name=request.getParameter("name");
		String relation=request.getParameter("relation");
		String dob=request.getParameter("dob");
		String age=request.getParameter("age");
		String blood=request.getParameter("blood");
		String gender=request.getParameter("gender");
		String nationality=request.getParameter("nationality");
		String profession=request.getParameter("profession");
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Attendance_db","root","manager");
			
			PreparedStatement ps=con.prepareStatement("insert into family(empId, username, name, relation, DOB, age, blood_group, gender, nationality, profession)value(?,?,?,?,?,?,?,?,?,?)");
			
			ps.setString(1, empid);
			ps.setString(2, username);
			ps.setString(3, name);
			ps.setString(4, relation);
			ps.setString(5, dob);
			ps.setString(6, age);
			ps.setString(7, blood);
			ps.setString(8, gender);
			ps.setString(9, nationality);
			ps.setString(10, profession);
			
			ps.executeUpdate();
			
			response.sendRedirect("salary.jsp");
		}  
		
		catch (Exception e) 
		{
			e.printStackTrace();
			}
	}

}
