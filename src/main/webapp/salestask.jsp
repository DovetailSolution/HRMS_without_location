<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.tcs.hr.AttendanceRecord" %>
<%@page import="java.util.List"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sales Task Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 100%;
            margin: 0 auto;
            background: #fff;
            padding: 20px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }
        h2 {
            text-align: center;
            color: #333;
        }
        form {
            margin-bottom: 20px;
        }
        input[type="text"],
        input[type="email"],
        select {
            width: calc(100% - 22px);
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        button {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #0056b3;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #007bff;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>
    <% 
    boolean meetingStarted = session.getAttribute("meetingStarted") != null;
    String meetingInTime = (String) session.getAttribute("meetingIn");
%>

<div class="container">
    <h2>Enter Client Details</h2>

    <form action="salestask" method="post">
        Company Name: <input type="text" name="companyName" placeholder="Enter Company Name" required><br>
        Company Address: <input type="text" name="companyAddress" placeholder="Enter Company Address" required><br>
        Client Name: <input type="text" name="clientName" placeholder="Enter Client Name" required><br>
        <button type="submit" name="action" value="meetingin">Meeting In</button>
    </form>

    <% if (meetingStarted) { %>
    <form action="salestask" method="post">
        <h3>Meeting In Time: <%= meetingInTime %></h3>
        Designation: <input type="text" name="designation" placeholder="Enter Designation" required><br>
        Mobile No: <input type="text" name="mobileNo" placeholder="Enter Mobile No" required><br>
        Email: <input type="email" name="email" placeholder="Enter Email" required><br>
        Work assigned:
        <select id="work" name="work">
            <option value="website">Website</option>
            <option value="App">App</option>
            <option value="digital">Digital</option>
        </select><br>
        Amount received: <input type="text" name="amount" placeholder="Enter Amount Received" required><br>
        Remarks: <input type="text" name="remarks" placeholder="Enter Remarks" required><br> 
        <button type="submit" name="action" value="meetingout">Submit</button>
    </form>
    <% } %>

    <form action="salestask" method="get">
        <button type="submit" name="action" value="viewDetails">View Client Details</button>
    </form>

    <h2>Client Details</h2>

    <table>
        <tr>
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
            <th>Remarks</th>
            <th>Status</th>
        </tr>
        <% 
            List<AttendanceRecord> records = (List<AttendanceRecord>) request.getAttribute("saletask");
            if (records != null && !records.isEmpty()) {
                for (AttendanceRecord record : records) {
        %>
        <tr>
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
            <td><%= record.getRemarks() %></td>
            <td><%= record.getSalestaskstatus() %></td>
        </tr>
        <% 
                }
            } else {
        %>
        <tr>
            <td colspan="12" style="text-align:center;">No records found</td>
        </tr>
        <% 
            }
        %>
    </table>
</div>

</body>
</html>
