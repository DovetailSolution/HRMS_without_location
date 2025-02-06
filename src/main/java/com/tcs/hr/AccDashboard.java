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

@WebServlet(name = "accdashboard", urlPatterns = { "/accdashboard" })
public class AccDashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AccDashboard() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db","root","manager");
			
			PreparedStatement ps=con.prepareStatement("select * from expenses where status='Approved_by_admin'");
			ResultSet rs=ps.executeQuery();
			
			List<ExpenseRecord> records=new ArrayList<ExpenseRecord>();
			while(rs.next())
			{
				ExpenseRecord record=new ExpenseRecord();
				record.setId(rs.getInt("id"));
				record.setEmpid(rs.getString("empid"));
                record.setUsername(rs.getString("username"));
                record.setTravel_route(rs.getString("travel_route"));
                record.setDate(rs.getDate("date"));
                record.setTimefrom(rs.getTime("time_from"));
                record.setTimeto(rs.getTime("time_to"));
                record.setPurpose(rs.getString("purpose"));
                record.setProject(rs.getString("project"));
                record.setExpenseincurred(rs.getDouble("expenses_incurred"));
                record.setAdvancetaken(rs.getDouble("advance_taken"));
                record.setMode(rs.getString("mode"));
                record.setTicketNo(rs.getString("ticket_no"));
                record.setTicketdate(rs.getDate("ticket_date"));
                record.setAttachment(rs.getString("attachment_path"));
                record.setStatus(rs.getString("status"));
                
                records.add(record);
			}
			
			request.setAttribute("expensesdetails", records);
			request.getRequestDispatcher("accexpense.jsp").forward(request, response);
		
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	String empid=request.getParameter("empId");
	String expenseid=request.getParameter("expenseId");
	String action=request.getParameter("action");
	
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db","root","manager");
		
		if("issued".equals(action))
		{
		PreparedStatement ps=con.prepareStatement("update expenses set status=? where id=?");
		ps.setString(1, action);
		ps.setString(2, expenseid);
		
		ps.executeUpdate();
		
		}
		response.sendRedirect("AccountantDashboard.jsp");
	} 
	catch (Exception e) {
		e.printStackTrace();
	}
	}

}
