<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.sql.Timestamp, java.util.List, com.tcs.hr.AttendanceRecord" %>
<%@ page session="false" %> <!-- Prevent session caching if applicable -->
<% response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); %>
<% response.setHeader("Pragma", "no-cache"); %>
<% response.setDateHeader("Expires", 0); %>

<!DOCTYPE html>
<html>
<head>
    <title>Status</title>
    <style>
        body {
            background-image: url("status3.png");
            background-size: cover;
            background-repeat: no-repeat;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f0f0f0;
        }
        .container {
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }
        h2 {
            text-align: center;
            color: #333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f4f4f4;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        p {
            font-size: 16px;
            color: #555;
        }
    </style>
</head>
<body>
	
    <div class="container">
        <h2>Leave Status</h2>
        
        <%
            List<AttendanceRecord> leaveRequests = (List<AttendanceRecord>) request.getAttribute("leaveRequests");
            if (leaveRequests != null && !leaveRequests.isEmpty()) {
        %>
        <h3>Your Leave Requests</h3>
        <table>
            <tr>
                <th>Leave Type</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Status</th>
                <th>Requester</th>
                <th>Leave Payment Type</th> <!-- Add this column -->
            </tr>
            <%
                for (AttendanceRecord leaveRequest : leaveRequests) {
            %>
            <tr>
                <td><%= leaveRequest.getLeaveType() %></td>
                <td><%= leaveRequest.getStartdate() %></td>
                <td><%= leaveRequest.getEnddate() %></td>
                <td><%= leaveRequest.getStatus() %></td>
                <td><%= leaveRequest.getRequester() %>
                <td><%= leaveRequest.getLeavePaymentType() %></td> <!-- Add this cell -->
            </tr>
            <%
                }
            %>
        </table>
        <%
            } else {
        %>
        <p>No leave requests found.</p>
        <%
            }
        %>
    </div>
</body>
</html>
