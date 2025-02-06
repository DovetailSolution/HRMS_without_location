package com.tcs.hr;

import java.io.File;

import java.io.IOException;
import com.tcs.hr.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.mail.SendFailedException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.Integer;

@WebServlet("/admin")
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Admin() {
		super();
	}
	private static final String UPLOAD_DIRECTORY = "uploads/";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String empid=request.getParameter("empId");
		String action=request.getParameter("action");
		String expenseid=request.getParameter("expenseId");
		
//---------------------------------------------------------------------------------------------------
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root",
					"manager");
			
			if(empid!=null && "Approved_by_admin".equals(action))
			{
			PreparedStatement ps=con.prepareStatement("update expenses set status=? where id=? ");
			ps.setString(1, action);
			ps.setString(2, expenseid);
			
			ps.executeUpdate();
			
			ps.close();
			}
			
			else if("Rejected_by_Admin".equals(action))
			{
				PreparedStatement ps=con.prepareStatement("update expenses set status=? where id=? ");
				ps.setString(1, action);
				ps.setString(2, expenseid);
				
				ps.executeUpdate();
				
				ps.close();
			}
			
			response.sendRedirect("adminpanel.jsp");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
  //--------------------------------------------------------------------------------------------------
		if ("contactInfo".equals(action)) 
		{
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root",
						"manager");

				PreparedStatement ps = con.prepareStatement(
						"select users.empId, users.username, users.mobile, users.email, salary.joining_date from users join salary on users.empId=salary.empId");
				ResultSet rs = ps.executeQuery();

				List<AttendanceRecord> records = new ArrayList<AttendanceRecord>();
				while (rs.next()) {
					AttendanceRecord record = new AttendanceRecord();
					record.setEmpId(rs.getString("empId"));
					record.setUsername(rs.getString("username"));
					record.setMobile(rs.getInt("mobile"));
					record.setEmail(rs.getString("email"));
					record.setJoiningdate(rs.getDate("joining_date"));

					records.add(record);
				}

				// Set the list of records as an attribute to be accessed in the JSP
				request.setAttribute("contact", records);
				request.getRequestDispatcher("adminpanel.jsp").forward(request, response);

				con.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		} 
//-----------------------------------------------------------------------------------------------------		
		else if ("attendance".equals(action)) 
		{
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root",
						"manager");

				String query = "select u.username, u.empId, a.sign_in_time, a.sign_out_time, a.sign_out_location, a.location "
						+ "from attendance a join users u on u.empId = a.empId";

				PreparedStatement ps = con.prepareStatement(query);

				ResultSet rs = ps.executeQuery();

				List<AttendanceRecord> records = new ArrayList<AttendanceRecord>();
				while (rs.next()) {
					AttendanceRecord record = new AttendanceRecord();
					record.setEmpId(rs.getString("empId"));
					record.setUsername(rs.getString("username"));
					record.setSignInTime(rs.getTimestamp("sign_in_time"));
					record.setSignOutTime(rs.getTimestamp("sign_out_time"));
					record.setSignOutLocation(rs.getString("sign_out_location"));
					record.setLocation(rs.getString("location"));

					records.add(record); // Adding the record to the list
				}

				request.setAttribute("attendance", records);
				request.getRequestDispatcher("adminpanel.jsp").forward(request, response);

				con.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		} 
//-----------------------------------------------------------------------------------------------------		
		else if ("leave".equals(action)) 
		{

			HttpSession session = request.getSession();
			String empid = (String) session.getAttribute("empid");
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root",
						"manager");

				PreparedStatement ps = con.prepareStatement("select * from leave_requests");
				// ps.setString(1, empid);

				ResultSet rs = ps.executeQuery();

				System.out.println(empid);
				List<AttendanceRecord> records = new ArrayList<AttendanceRecord>();
				while (rs.next()) {
					AttendanceRecord record = new AttendanceRecord();
					record.setId(rs.getInt("id"));
					record.setEmpId(rs.getString("empId"));
					record.setLeaveType(rs.getString("leave_type"));
					record.setStartDate(rs.getTimestamp("start_date"));
					record.setEndDate(rs.getTimestamp("end_date"));
					record.setRequester(rs.getString("requester"));
					record.setStatus(rs.getString("status"));
					record.setLeavePaymentType(rs.getString("leave_payment_type"));

					records.add(record);
				}

				request.setAttribute("leave", records);
				request.getRequestDispatcher("adminpanel.jsp").forward(request, response);

				con.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		} 
//---------------------------------------------------------------------------------------------------		
		else if ("admindashboard".equals(action)) 
		{
			try {

				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root",
						"manager");
				PreparedStatement ps = con.prepareStatement("select count(distinct username) from users");

				ResultSet rs = ps.executeQuery();

				int usercount = 0;
				if (rs.next()) {
					usercount = rs.getInt(1); // retrieves the value of the first column in the current rows of the
												// resultset.
				}

				request.setAttribute("count", usercount);
				request.getRequestDispatcher("adminpanel.jsp").forward(request, response);

				con.close();
			} catch (Exception e) {

				e.printStackTrace();

			}
		}

//-----------------------------------------------------------------------------------------------------		
		 else if("salary".equals(action)) 
		 { 
			 try {
		 Class.forName("com.mysql.cj.jdbc.Driver"); Connection
		 con=DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db",
		  "root", "manager");
		  
		  PreparedStatement ps=con.
		  prepareStatement("select users.role, salary.* from users join salary on users.empId=salary.empId;"
		  );
		  
		  ResultSet rs=ps.executeQuery();
		  
		  List<AttendanceRecord> records=new ArrayList<AttendanceRecord>();
		  
		  while(rs.next()) { AttendanceRecord record=new AttendanceRecord();
		  record.setRole(rs.getString("role")); record.setEmpId(rs.getString("empId"));
		  record.setName(rs.getString("name"));
		  record.setDepartment(rs.getString("department"));
		  record.setExperience(rs.getInt("experience"));
		  record.setMonthly(rs.getBigDecimal("monthly_sal"));
		  
		  records.add(record); }
		  
		  request.setAttribute("salary", records);
		  request.getRequestDispatcher("adminpanel.jsp").forward(request, response);
		  
		  con.close(); 
		  } 
			 catch (Exception e) 
			 {
				 e.printStackTrace(); 
		  } 
			 }

//----------------------------------------------------------------------------------------------------
		else if ("employeeInfo".equals(action)) {
			try {

				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root",
						"manager");

				PreparedStatement ps = con.prepareStatement("select * from users");
				ResultSet rs = ps.executeQuery();

				List<AttendanceRecord> records = new ArrayList<AttendanceRecord>();
				while (rs.next()) {
					AttendanceRecord record = new AttendanceRecord();
					record.setEmpId(rs.getString("empId"));
					record.setUsername(rs.getString("username"));
					record.setRole(rs.getString("role"));
					record.setGender(rs.getString("gender"));
					record.setDob(rs.getDate("dob"));
					record.setEmail(rs.getString("email"));
					record.setMobile(rs.getInt("mobile"));
					record.setNationality(rs.getString("nationality"));
					record.setBlood(rs.getString("blood"));

					records.add(record);
				}

				request.setAttribute("employees", records);
				request.getRequestDispatcher("adminpanel.jsp").forward(request, response);

				con.close();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}

//-----------------------------------------------------------------------------------------------------
		else if ("bank".equals(action)) 
		{
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root",
						"manager");
				PreparedStatement ps = con.prepareStatement("select * from salary");

				ResultSet rs = ps.executeQuery();
				List<AttendanceRecord> records = new ArrayList<AttendanceRecord>();

				while (rs.next()) {
					AttendanceRecord record = new AttendanceRecord();
					record.setBankname(rs.getString("bank_name"));
					record.setBankaccountno(rs.getString("bank_account_no"));
					record.setName(rs.getString("name"));
					record.setIfsc(rs.getString("ifsc"));
					records.add(record);
				}

				request.setAttribute("bank", records);
				request.getRequestDispatcher("adminpanel.jsp").forward(request, response);

				con.close();
			} 
			catch (Exception e) 
			{

				e.printStackTrace();
			}
		}

//-------------------------------------------------------------------------------------------------------
		else if ("family".equals(action)) {

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Attendance_db", "root",
						"manager");

				PreparedStatement ps = con.prepareStatement("select * from family");
				ResultSet rs = ps.executeQuery();

				List<AttendanceRecord> records = new ArrayList<AttendanceRecord>();

				while (rs.next()) {
					AttendanceRecord record = new AttendanceRecord();
					record.setEmpId(rs.getString("empId"));
					record.setUsername(rs.getString("username"));
					record.setFname(rs.getString("name"));
					record.setRelation(rs.getString("relation"));
					record.setFdob(rs.getDate("dob"));
					record.setAge(rs.getInt("age"));
					record.setFblood(rs.getString("blood_group"));
					record.setFgender(rs.getString("gender"));
					record.setFnationality(rs.getString("nationality"));
					record.setProfession(rs.getString("profession"));

					records.add(record);

				}
				request.setAttribute("family", records);
				request.getRequestDispatcher("adminpanel.jsp").forward(request, response);

				con.close();
			} 
			catch (Exception e) 
			{

				e.printStackTrace();
			}
		} 
		
//-----------------------------------------------------------------------------------------------------
		else if ("quotations".equals(action)) 
		{
			response.sendRedirect("quotation.jsp");
		} 
		
//------------------------------------------------------------------------------------------------------
		else if ("sale".equals(action)) 
		{
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Attendance_db", "root",
						"manager");

				PreparedStatement ps = con.prepareStatement("select * from client");
				ResultSet rs = ps.executeQuery();

				List<AttendanceRecord> records = new ArrayList<>();
				while (rs.next()) {
					AttendanceRecord record = new AttendanceRecord();
					record.setTaskid(rs.getInt("id"));
					record.setUsername(rs.getString("username"));
					record.setSaledate(rs.getDate("date"));
					record.setCompanyname(rs.getString("company_name"));
					record.setCompanyaddress(rs.getString("company_address"));
					record.setClient(rs.getString("client_name"));
					record.setClientdesignation(rs.getString("designation"));
					record.setClientmobile(rs.getString("mobile"));
					record.setClientemail(rs.getString("email"));
					record.setWork(rs.getString("work"));
					record.setClientamount(rs.getInt("Amount_received"));
					record.setMeetingIn(rs.getString("meeting_in"));
					record.setMeetingOut(rs.getString("meeting_out"));
					record.setSalestaskstatus(rs.getString("status"));

					records.add(record);
				}
				request.setAttribute("saletasks", records);
				request.getRequestDispatcher("adminpanel.jsp").forward(request, response);
			}

			catch (Exception e)
			{
				e.printStackTrace();
			}
		} 
		
//-------------------------------------------------------------------------------------------------------
		else if ("reports".equals(action)) 
		{
		    String reportType = request.getParameter("report-type");

		    if (reportType == null) {
		        // No report type selected yet, so show report buttons
		        request.setAttribute("showReportButtons", true);
		        request.getRequestDispatcher("adminpanel.jsp").forward(request, response);
		    } 
		    else {
		        // Report type is selected, proceed to generate the report
		        response.setContentType("text/csv");
		        response.setHeader("Content-Disposition", "attachment; filename=\"" + reportType + "_Report.csv\"");

		        try {
		            Class.forName("com.mysql.cj.jdbc.Driver");
		            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

		            if ("attendance".equals(reportType)) {
		                ReportGenerator.generateAttendanceReport(response, con);
		            } else if ("salary".equals(reportType)) {
		                ReportGenerator.generateSalaryReport(response, con);
		            } else if ("employee".equals(reportType)) {
		                ReportGenerator.generatePivotedReport(response, con);
		            }else if ("expenses".equals(reportType)) {
		            	ReportGenerator.generateExpenseReport(response, con);
		            }

		            con.close();
		        }
		        catch (Exception e) 
		        {
		            e.printStackTrace();
		        }
		    }
		}
		
//-----------------------------------------------------------------------------------------------------
		else if("expenses".equals(action))
		{
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db","root","manager");
			
				PreparedStatement ps=con.prepareStatement("select * from expenses where status='Send_To_Admin'");
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
	                String attachmentPath = rs.getString("attachment_path");
	                if (attachmentPath != null) {
	                    // Extract the file name from the attachment path
	                    String fileName = new File(attachmentPath).getName();

	                    // Encode only spaces and special characters
	                    String encodedFileName = fileName.replace(" ","%20");
	                    
	                    // Construct the server file URL
	                    String serverFileUrl = response.encodeURL(request.getContextPath() + "/" + UPLOAD_DIRECTORY + encodedFileName);

	                    record.setAttachment(serverFileUrl);
	                }
	                record.setStatus(rs.getString("status"));
	                
	                records.add(record);
				}
					request.setAttribute("expensedetails", records);
					request.getRequestDispatcher("adminpanel.jsp").forward(request, response);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}

	}
	
}
