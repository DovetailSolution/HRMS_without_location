<%@page import="com.tcs.hr.ExpenseRecord"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tcs.hr.AttendanceRecord" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Panel</title>
<style>
    body {
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 0;
        display: flex;
        height: 100vh;
    }

    .sidebar {
        width: 20%;
        background-color: #2c3e50;
        height: 100%;
        padding: 20px;
        box-shadow: 2px 0 5px rgba(0,0,0,0.1);
    }

    .sidebar h2 {
        margin-top: 0;
        color: white;
    }

    .sidebar button {
        width: 100%;
        padding: 10px;
        margin: 10px 0;
        background-color: #34495e;
        color: white;
        border: none;
        cursor: pointer;
        text-align: left;
        border-radius: 4px;
    }

    .sidebar button:hover {
        background-color: #1abc9c;
    }

    .content {
        width: 80%;
        padding: 20px;
        overflow-y: auto;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }

    table, th, td {
        border: 1px solid #ccc;
    }

    th, td {
        padding: 10px;
        text-align: left;
    }

    th {
        background-color: #f2f2f2;
    }

    .button-container {
        display: flex;
        justify-content: flex-end;
        margin-bottom: 10px;
    }

    .button-container a {
        padding: 10px 20px;
        background-color: #3498db;
        color: white;
        text-decoration: none;
        border-radius: 4px;
        text-align: center;
        transition: background-color 0.3s ease;
    }

    .button-container a:hover {
        background-color: #2980b9;
    }
    button[name="action"][value="reject"] {
        background-color: #e74c3c;
    }
     button[name="action"][value="Approved_by_admin"] {
        background-color: #4CAF50;
    }
</style>
<script>
    function loadModule(moduleName) {
        document.getElementById("moduleAction").value = moduleName;
        document.getElementById("moduleForm").submit();
    }
    
    function showDownloadButtons() {
        document.getElementById("report-buttons").style.display = 'block';
    }
</script>
</head>
<body>

<div class="sidebar">
    <h2>Admin Panel</h2>
    <button onclick="loadModule('admindashboard')">Admin Dashboard</button>
    <button onclick="loadModule('employeeInfo')">Employee Info</button>
    <button onclick="loadModule('contactInfo')">Employee Directory</button>
    <button onclick="loadModule('bank')">Bank Details</button>
    <button onclick="loadModule('attendance')">Attendance</button>
    <button onclick="loadModule('leave')">Leaves</button>
    <button onclick="loadModule('salary')">Payroll</button>
    <button onclick="loadModule('reports')">Reports</button>
    <button onclick="loadModule('family')">Family Details</button>
    <button onclick="loadModule('quotations')">Invoices</button>
    <button onclick="loadModule('sale')">Sale tasks</button>
    <button onclick="loadModule('expenses')">Expense Details</button>
    
</div>

<div class="content">
    <form id="moduleForm" action="admin" method="post" style="display:none;">
        <input type="hidden" id="moduleAction" name="action">
    </form>
 
 <!-- To get contact info------------------------------------------------------------------------>
    <% 
        List<AttendanceRecord> contact = (List<AttendanceRecord>) request.getAttribute("contact");
        if (contact != null ) {
    %>
        <h2>Employee Directory</h2>
        <table>
            <tr>
                <th>Employee ID</th>
                <th>Employee Name</th>
                <th>Phone</th>
                <th>Email</th>
                <th>Joining Date</th>
            </tr>
            <%
                for (AttendanceRecord record : contact) {
            %>
            <tr>
                <td><%= record.getEmpId() %></td>
                <td><%= record.getUsername() %></td>
                <td><%= record.getMobile() %></td>
                <td><%= record.getEmail() %></td>
                <td><%= record.getJoiningdate() %></td>
            </tr>
            <%
                }
            %>
        </table>
    <%
        
        } 
    %>
<!-- Fetch the attendance Info----------------------------------------------------------------------->
    <% 
        List<AttendanceRecord> attendance = (List<AttendanceRecord>) request.getAttribute("attendance");
        if (attendance != null && !attendance.isEmpty()) {
    %>
        <h2>Employees Attendance</h2>
        <table>
            <tr>
                <th>Employee Id</th>
                <th>Username</th>
                <th>Sign-in-time</th>
                <th>Sign-in-location</th>
                <th>Sign-out-time</th>
                <th>Sign-out-location</th>
            </tr>
            <%
                for (AttendanceRecord attend : attendance) {
            %>
            <tr>
                <td><%= attend.getEmpId() %></td>
                <td><%= attend.getUsername() %></td>
                <td><%= attend.getSignInTime() %></td>
                <td><%= attend.getLocation() %></td>
                <td><%= attend.getSignOutTime() %></td>
                <td><%= attend.getSignOutLocation() %></td>
            </tr>
            <%
                }
            %>
        </table>
    <%
        
        } 
    %>
<!--fetch the leave info-------------------------------------------------------------------------------->
    <% 
        List<AttendanceRecord> leave = (List<AttendanceRecord>) request.getAttribute("leave");
        if (leave != null && !leave.isEmpty()) {
    %>
        <h2>Leave Records</h2>
        <table>
            <tr>
                <th>Id</th>
                <th>Employee Id</th>
                <th>Leave Type</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Requester</th>
                <th>Status</th>
                <th>Leave Payment Type</th>
                <th>Edit</th>
            </tr>
            <%
                for (AttendanceRecord leaves : leave) {
            %> 
            <tr>
                <td><%= leaves.getId() %></td>
                <td><%= leaves.getEmpId() %></td>
                <td><%= leaves.getLeaveType() %></td>
                <td><%= leaves.getStartDate() %></td>
                <td><%= leaves.getEndDate() %></td>
                <td><%= leaves.getRequester() %></td>
                <td><%= leaves.getStatus() %></td>
                <td><%= leaves.getLeavePaymentType() %></td>
                <td>
                	<form action="updateleave" method="get">
                		<input type="hidden" name="id" value="<%= leaves.getId() %>">
                		<button type="submit">edit</button>
                	</form></td>
            </tr>
            <%
                }
            %>
        </table>
    <%
        
        } 
    %>
<!-- Fetch the Salary Info------------------------------------------------------------------------------>
    <% 
        List<AttendanceRecord> salary = (List<AttendanceRecord>) request.getAttribute("salary");
        if (salary != null && !salary.isEmpty()) {
    %>
        <h2>Salary Details</h2>
        <table>
            <tr>
                <th>Employee Id</th>
                <th>Name</th>
                <th>Department</th>
                <th>Designation</th>s
                <th>Experience (in months)</th>
                <th>Monthly Salary</th>
                <th>Edit</th>
            </tr>
            <%
                for (AttendanceRecord sal : salary) {
            %>
            <tr>
                <td><%= sal.getEmpId() %></td>
                <td><%= sal.getName() %></td>
                <td><%= sal.getDepartment() %></td>
                <td><%= sal.getRole() %></td>
                <td><%= sal.getExperience() %></td>
                <td><%= sal.getMonthly() %></td>
                <td>
                	<form action="updateSal" method="get">
                	<input type="hidden" name="empid" <%= sal.getEmpId() %>> 
                	<button type="submit">Edit</button>
                	</form></td>
            </tr>
            <%
                }
            %>
        </table>
    <%
        
        } 
    %>
 <!--Fetch the employee info------------------------------------------------------------------------->
    <%
    	List<AttendanceRecord> emp=(List<AttendanceRecord>) request.getAttribute("employees");
    	if(emp!=null)
    	{
    %>
    <h2>Employee details</h2>
    
    
    <div class="button-container">
        <a href="register.jsp">Add Employee</a>
    </div>
    
    <table>
    	<tr>
    		<th>Employee Id</th>
    		<th>Username</th>
    		<th>Role</th>
    		<th>Gender</th>
    		<th>Date of Birth</th>
    		<th>Email</th>
    		<th>Mobile</th>
    		<th>Nationality</th>
    		<th>Blood</th>
    		<th>Edit</th>
    	</tr>
    	<%
    		for(AttendanceRecord emp1: emp)
    		{
    	%>
    		<tr>
    			<td><%= emp1.getEmpId() %></td>
    			<td><%= emp1.getUsername() %></td>
    			<td><%= emp1.getRole() %></td>
    			<td><%= emp1.getGender() %></td>
    			<td><%= emp1.getDob() %></td>
    			<td><%= emp1.getEmail() %></td>
    			<td><%= emp1.getMobile() %></td>
    			<td><%= emp1.getNationality() %></td>
    			<td><%= emp1.getBlood() %></td>
    			<td>
    				<form action="update" method="get">
    					<input type="hidden" name="empId" id="empId" value="<%=emp1.getEmpId() %>">
    					<button type="submit">edit</button>
    				</form>
    			</td>
    		</tr>
    	<%
    		}
    	%>
    
    </table>
    <%
    }
    %>
  <!--Fetch the bank details--------------------------------------------------------------------------> 
    <%
    	List<AttendanceRecord> bank = (List<AttendanceRecord>) request.getAttribute("bank");
    	if(bank!=null)
    	{
    %>
    	<h2>Bank Account</h2>
		<table>
			<tr>
				<th>Username</th>
				<th>Bank Name</th>
				<th>Bank Account No </th>
				<th>IFSC Code</th>
			<tr>
		<%
			for(AttendanceRecord record : bank)
			{
		%>
			<tr>
				<td><%= record.getName() %></td>
				<td><%= record.getBankname() %></td>
				<td><%= record.getBankaccountno() %></td>
				<td><%= record.getIfsc() %></td>
			</tr>													
		<%												
			} 						
		%>						
		</table>						
		<%
			}
		%>
	<!--Fetch the family details ---------------------------------------------------------------------->	
		<%
			List<AttendanceRecord> family=(List<AttendanceRecord>) request.getAttribute("family");
			if(family!=null)
			{
		%>
		<h2>Family Details</h2>
		<table>
			<tr>
				<th>Employee Id</th>
				<th>Username</th>
				<th>Name</th>
				<th>Relation</th>
				<th>Date of Birth</th>
				<th>Age</th>
				<th>Blood Group</th>
				<th>Gender</th>
				<th>Nationality</th>
				<th>Profession</th>
			</tr>
			<%
				for(AttendanceRecord record : family)
				{
			%>
			
			<tr>
				<td><%= record.getEmpId() %></td>
				<td><%= record.getUsername() %></td>
				<td><%= record.getFname() %></td>
				<td><%= record.getRelation() %></td>
				<td><%= record.getFdob() %></td>
				<td><%= record.getAge() %></td>
				<td><%= record.getFblood() %></td>
				<td><%= record.getFgender() %></td>
				<td><%= record.getFnationality() %></td>
				<td><%= record.getProfession() %></td>
			</tr>
			<%
			}
			%>
			</table>
			<%
			}
			%>
	<!--Fetch the task details----------------------------------------------------------------------->
			<%
			
				List<AttendanceRecord> sale=  (List<AttendanceRecord>)request.getAttribute("saletasks");
				if(sale!=null)
				{
			%>
			<h2>Sale Performance</h2>
			<table>
				<tr>
				<th>Username</th>
				<th>Date</th>
				<th>Company Name</th>
        		<th>Company Address</th>
        		<th>Client Name</th>
        		<th>Designation</th>
        		<th>Mobile</th>
        		<th>Email</th>
       		 	<th>Work</th>
        		<th>Amount Received</th>
        		<th>Meeting In</th>
        		<th>Meeting Out</th>
        		<th>Status</th>
    		</tr>
			<%
				for(AttendanceRecord record:sale)
				{
			%>
			<tr>
				<td><%= record.getUsername() %></td>
				<td><%= record.getSaledate() %></td>
        		<td><%= record.getCompanyname() %></td>
        		<td><%= record.getCompanyaddress() %></td>
        		<td><%= record.getClient() %></td>
        		<td><%= record.getClientdesignation() %></td>
        		<td><%= record.getClientmobile() %></td>
        		<td><%= record.getClientemail() %></td>
        		<td><%= record.getWork() %></td>
        		<td><%= record.getClientamount() %></td>
        		<td><%= record.getMeetingIn() %></td>
        		<td><%= record.getMeetingOut() %></td>
        		<td><%= record.getSalestaskstatus() %></td>
       		</tr>
       				<%
			}
			%>
			</table>
			<%
			}
			%>
			
<!-- Fetch all the reports--------------------------------------------------------------------------->			
			
			<% if (request.getAttribute("showReportButtons") != null) { %>
    		<h3>Select a Report to Download:</h3>
    		<form action="admin" method="post">
        		<input type="hidden" name="action" value="reports">
        		<button type="submit" name="report-type" value="attendance">Download Attendance Report</button>
        		<button type="submit" name="report-type" value="salary">Download Salary Report</button>
        		<button type="submit" name="report-type" value="employee">Download Employee Report</button>
        		<button type="submit" name="report-type" value="expenses">Download Expenses Report</button>
    		</form>
			<% 
			} 
			%>
			
<!-- Fetch the expense details and approve------------------------------------------------------------->
			<%
				List<ExpenseRecord> expenses=(List<ExpenseRecord>) request.getAttribute("expensedetails");
				if(expenses!=null)
				{
			%>
			<h2>Expenses Details</h2>
				<table>
					<tr>
						<th>Id</th>
						<th>Emp ID</th>
	                    <th>Username</th>
	                    <th>Travel Route</th>
	                    <th>Date</th>
	                    <th>Time From</th>
	                    <th>Time To</th>
	                    <th>Purpose</th>
	                    <th>Project</th>
	                    <th>Expenses Incurred</th>
	                    <th>Advance Taken</th>
	                    <th>Mode of Travel</th>
	                    <th>Ticket No.</th>
	                    <th>Ticket Date</th>
	                    <th>Attachment</th>
	                    <th>Actions</th>
	                  </tr>
					<%
						for(ExpenseRecord record: expenses)
						{
					%>
					<tr>
						<td><%= record.getId() %></td>
						<td><%= record.getEmpid() %></td>
	                    <td><%= record.getUsername() %></td>
	                    <td><%= record.getTravel_route() %></td>
	                    <td><%= record.getDate() %></td>
	                    <td><%= record.getTimefrom() %></td>
	                    <td><%= record.getTimeto() %></td>
	                    <td><%= record.getPurpose() %></td>
	                    <td><%= record.getProject() %></td>
	                    <td><%= record.getExpenseincurred() %></td>
	                    <td><%= record.getAdvancetaken() %></td>
	                    <td><%= record.getMode() %></td>
	                    <td><%= record.getTicketNo() %></td>
	                    <td><%= record.getTicketdate() %></td>
	                    <td>
					    <% if (record.getAttachment() != null && !record.getAttachment().isEmpty()) { %>
					        <a href="<%= record.getAttachment() %>" target="_blank">Open Attachment</a>

					    <% } else { %>
					        No Attachment
					    <% } %>
					</td>
						<td>
							<form action="admin" method="get" style="display: flex; gap: 5px;">
								<input type="hidden" name="empId" value="<%= record.getEmpid() %>">
								<input type="hidden" name="expenseId" value="<%= record.getId() %>">
							    <button type="submit" name="action" value="Approved_by_admin" >Approve</button>
							    <button type="submit" name="action" value="Rejected_by_Admin" >Reject</button>
							</form>
						</td>
						</tr>
						<%
						}
						%>
				</table>
				<%
				}
				%>
</body>
</html>
