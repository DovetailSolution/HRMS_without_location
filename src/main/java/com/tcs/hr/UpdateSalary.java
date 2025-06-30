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
import javax.servlet.http.HttpSession;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

@WebServlet(name = "updateSal", urlPatterns = { "/updateSal" })
public class UpdateSalary extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UpdateSalary() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String empid=request.getParameter("empid");
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
			
			PreparedStatement ps=con.prepareStatement("select users.role, salary.* from users join salary on users.empId=salary.empId where salary.empId=?");
			ps.setString(1, empid);
			
			ResultSet rs=ps.executeQuery();
			
			List<AttendanceRecord> records=new ArrayList<AttendanceRecord>();
			while(rs.next())
			{
				AttendanceRecord record=new AttendanceRecord();
				record.setEmpId(rs.getString("empId"));
				record.setRole(rs.getString("role"));
				
				records.add(record);
			}
			
			request.setAttribute("salary", records);
			request.getRequestDispatcher("updatesalary.jsp").forward(request, response);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String empid=request.getParameter("empid");
		String role=request.getParameter("role");

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db","root","manager");
			
			PreparedStatement ps=con.prepareStatement("update users set role=? where empId=?");
			
			ps.setString(1, role);
			ps.setString(2, empid);
			
			ps.executeUpdate();
		
			request.getRequestDispatcher("adminpanel.jsp").forward(request, response);
			
			con.close();
		} 
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
