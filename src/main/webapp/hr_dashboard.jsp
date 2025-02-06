<%@ page import="java.util.List" %>
<%@ page import="com.tcs.hr.AttendanceRecord" %>
<%@ page import="com.tcs.hr.ExpenseRecord" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HR Dashboard</title>
    <style>
        body {
            background-image: url("hr image.jpg");
            background-size: cover;
            font-family: 'Roboto', sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 100%;
            margin: 50px auto;
            padding: 30px;
            background-color: rgba(255, 255, 255, 0.95);
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
            border-radius: 10px;
        }
        h2 {
            text-align: center;
            margin-bottom: 30px;
            color: #444;
            font-size: 28px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
            font-size: 16px;
        }
        th, td {
            padding: 15px;
            border: 1px solid #ddd;
            text-align: center;
        }
        th {
            background-color: #007BFF;
            color: #fff;
            font-weight: 600;
        }
        tr:nth-child(even) {
            background-color: #f8f8f8;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .button {
            display: inline-block;
            padding: 12px 24px;
            margin-right: 10px;
            background-color: #007BFF;
            color: #fff;
            text-decoration: none;
            border-radius: 6px;
            text-align: center;
            font-size: 16px;
            transition: background-color 0.3s ease;
        }
        .button:hover {
            background-color: #0056b3;
        }
        .button-secondary {
            background-color: #007BFF;
        }
        .button-secondary:hover {
            background-color: #495057;
        }
        .form-container {
            text-align: center;
            margin-bottom: 30px;
        }
        .form-container form {
            display: inline-block;
            margin-bottom: 30px;
        }
        .form-container input[type="month"] {
            padding: 12px;
            margin-right: 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 16px;
        }
        .form-container button {
            padding: 12px 24px;
            background-color: #007BFF;
            color: #fff;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        .form-container button:hover {
            background-color: #0056b3;
        }
        .button-group {
            text-align: center;
            margin-top: 20px;
        }
        .button-group form {
            display: inline-block;
            margin-right: 10px;
        }
        .button-group a {
            margin-right: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Attendance Records</h2>
        <div class="form-container">
            <form action="hr" method="get">
                <label for="yearMonth">Select Month and Year: </label>
                <input type="month" id="yearMonth" name="yearMonth" required>
                <button type="submit">Fetch Report</button>
            </form>
        </div>
 <!----------------- Fetch the attendance Details------------------------------------------------------>
        <table>
            <thead>
                <tr>
                    <th>Emp ID</th>
                    <th>Username</th>
                    <th>Sign-In Time</th>
                    <th>Sign-In Location</th>
                    <th>Sign-Out Time</th>
                    <th>Sign-Out Location</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    List<AttendanceRecord> records = (List<AttendanceRecord>) request.getAttribute("attendanceRecords");
                    if (records != null) {
                        for (AttendanceRecord record : records) {
                %>
                <tr>
                    <td><%= record.getEmpId() %></td>
                    <td><%= record.getUsername() %></td>
                    <td><%= (record.getSignInTime() != null) ? record.getSignInTime().toString() : "" %></td>
                    <td><%= record.getLocation() %></td>
                    <td><%= (record.getSignOutTime() != null) ? record.getSignOutTime().toString() : "" %></td>
                    <td><%= record.getSignOutLocation() %></td>
                </tr>
                <% 
                        }
                    } else {
                %>
                <tr>
                    <td colspan="6">No attendance records found</td>
                </tr>
                <% 
                    }
                %>
            </tbody>
        </table>
        
<!--------------------Approve or reject the leave reqeust---------------------------------------------->

        <h2>Leave Requests</h2>
        <table>
            <thead>
                <tr>
                    <th>Emp ID</th>
                    <th>Username</th>
                    <th>Leave Type</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Requester</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    List<AttendanceRecord> leaveRequests = (List<AttendanceRecord>) request.getAttribute("leaveRequests");
                    if (leaveRequests != null) {
                        for (AttendanceRecord leave : leaveRequests) {
                %>
                <tr>
                    <td><%= leave.getEmpId() %></td>
                    <td><%= leave.getUsername() %></td>
                    <td><%= leave.getLeaveType() %></td>
                    <td><%= leave.getStartDate() %></td>
                    <td><%= leave.getEndDate() %></td>
                    <td><%= leave.getRequester() %></td>
                    <td><%= leave.getStatus() %></td>
                    <td>
                        <form action="hr" method="post" style="display:inline;">
                            <input type="hidden" name="leaveId" value="<%= leave.getId() %>">
                            <label for="leavePaymentType">Leave Payment Type: </label>
                            <select name="leavePaymentType" required>
                                <option value="Paid">Paid</option>
                                <option value="Unpaid">Unpaid</option>
                            </select>
                            <button type="submit" name="decision" value="Approved" class="button">Approve</button>
                            <button type="submit" name="decision" value="Rejected" class="button button-secondary">Reject</button>
                        </form>
                    </td>
                </tr>
                <% 
                        }
                    } else {
                %>
                <tr>
                    <td colspan="8">No pending leave requests</td>
                </tr>
                <% 
                    }
                %>
            </tbody>
        </table>
        
  <!-----------------------------Approve or reject the slip reqeust------------------------------->
  
        <h2>Salary Slip Requests</h2>
        <table>
            <thead>
                <tr>
                    <th>Emp ID</th>
                    <th>Username</th>
                    <th>Request Date</th>
                    <th>Slip for (month)</th>
                    <th>Approval Status</th>
                    <th>Actioned Date</th>
                    <th>Remarks</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    List<AttendanceRecord> salaryRequests = (List<AttendanceRecord>) request.getAttribute("salarySlipRequests");
                    if (salaryRequests != null) {
                        for (AttendanceRecord record1 : salaryRequests) {
                %>
                <tr>
                    <td><%= record1.getEmpId() %></td>
                    <td><%= record1.getUsername() %></td>
                    <td><%= (record1.getRequestDate() != null) ? record1.getRequestDate().toString() : "" %></td>
                    <td><%= record1.getSlipMonth() %></td>
                    <td><%= record1.getSlipstatus() %></td>
                    <td><%= (record1.getActionedDate() != null) ? record1.getActionedDate().toString() : "" %></td>
                    <td><%= (record1.getRemarks() != null) ? record1.getRemarks() : "" %></td>
                    <td>
                        <form action="hr" method="post" style="display:inline;">
                            <input type="hidden" name="empId" value="<%= record1.getEmpId() %>">
                            <input type="hidden" name="requestId" value="<%= record1.getId() %>">
                            <button type="submit" name="action" value="approve" class="button">Approve</button>
                            <button type="submit" name="action" value="reject" class="button button-secondary">Reject</button>
                        </form>
                    </td>
                </tr>
                <% 
                        }
                    } else {
                %>
                <tr>
                    <td colspan="8">No salary slip requests pending</td>
                </tr>
                <% 
                    }
                %>
            </tbody>
        </table>
        
<!---------------------------Approve, reject or send to admin of expense approval-------------->

        <h2>Expense Approval</h2>
        <table>
            <thead>
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
            </thead>
            <tbody>
                <% 
                    List<ExpenseRecord> expenseRecord = (List<ExpenseRecord>) request.getAttribute("ExpenseDetails");
                    if (expenseRecord != null) {
                        for (ExpenseRecord record : expenseRecord) {
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
					    <form action="hr" method="post" style="display:inline;">
					        <input type="hidden" name="empId" value="<%= record.getEmpid() %>">
					        <input type="hidden" name="expenseid" value="<%= record.getId() %>">
					        <button type="submit" name="action" value="Approved_by_HR" class="button">Approve</button>
					        <button type="submit" name="action" value="Rejected_by_HR" class="button button-secondary">Reject</button>
					        <button type="submit" name="action" value="Send_To_Admin" class="button button-secondary">Send to Admin</button>
					    </form>
					</td>

                </tr>
                <% 
                        }
                    } else {
                %>
                <tr>
                    <td colspan="15">No expense records found</td>
                </tr>
                <% 
                    }
                %>
            </tbody>
        </table>
 <!---------------------------------------------------------------------------------------------->
    </div>
    <div class="button-group">
        <form action="report" method="get">
            <button type="submit" name="action" value="download1" class="button">Download Employee Report</button>
            <button type="submit" name="action" value="download2" class="button">Download Attendance Report</button>
            <button type="submit" name="action" value="download3" class="button">Download Salary Report</button>
        </form>
        <a href="register.jsp" class="button button-secondary">Register User</a>
        <a href="salary.jsp" class="button button-secondary">Boarding</a>
        
        <form action="salarymodule" method="post" style="display:inline;">
            <button type="submit" class="button">Calculate Salaries for All Employees</button>
        </form>
        <a href="relieving_letter_template.jsp" class="button button-secondary">Relieving Letter</a>
    	<a href="joining_letter_template.jsp" class="button button-secondary">Joining Letter</a>
    	<a href="notice.jsp" class="button button-secondary">Notice</a>
    	<form action="salary" method="get" style="display:inline;">
            <button type="submit" class="button">View details</button>
        </form>
    </div>
</body>
</html>
