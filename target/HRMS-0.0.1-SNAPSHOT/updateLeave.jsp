<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tcs.hr.AttendanceRecord" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Leave Records</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .edit-container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 600px;
        }

        .edit-container h2 {
            margin-top: 0;
            text-align: center;
            color: #333;
        }

        .edit-container label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: bold;
        }

        .edit-container input[type="text"],
        .edit-container select {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .edit-container button {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
		
        .edit-container button:hover {
            background-color: #0056b3;
        }

        .edit-container hr {
            border: 0;
            height: 1px;
            background: #ccc;
            margin: 20px 0;
        }

        .edit-container p {
            text-align: center;
            color: #e74c3c;
        }
    </style>
</head>
<body>

<div class="edit-container">
    <h2>Edit Leave Records</h2>

    <%
    List<AttendanceRecord> leaves = (List<AttendanceRecord>) request.getAttribute("leaves");
    if (leaves != null && !leaves.isEmpty()) {
        for (AttendanceRecord leave : leaves) {
    %>
    
    <form action="updateleave" method="post">
    
        <input type="hidden" name="id" value="<%= leave.getId() %>">
        <input type="hidden" name="empId" value="<%= leave.getEmpId() %>">

        <label for="leaveType">Leave Type</label>
        <input type="text" id="leaveType" name="leaveType" value="<%= leave.getLeaveType() %>">

        <label for="startDate">Start Date</label>
        <input type="text" id="startDate" name="startDate" value="<%= leave.getStartDate() %>">

        <label for="endDate">End Date</label>
        <input type="text" id="endDate" name="endDate" value="<%= leave.getEndDate() %>">

        <label for="requester">Requester</label>
        <input type="text" id="requester" name="requester" value="<%= leave.getRequester() %>">

        <label for="status">Status</label>
        <select name="status" id="status">
        	<option value="approved" <%= "approved".equals(leave.getStatus()) ? "selected" : "" %>>Approved</option>
        	<option value="rejected" <%= "rejected".equals(leave.getStatus()) ? "selected" : "" %>>Rejected</option>
 		</select>
 		
        <label for="leavePaymentType">Leave Payment Type</label>
        <select name="leavePaymentType" id="leavePaymentType">
            <option value="paid" <%= "paid".equals(leave.getLeavePaymentType()) ? "selected" : "" %>>Paid</option>
            <option value="unpaid" <%= "unpaid".equals(leave.getLeavePaymentType()) ? "selected" : "" %>>Unpaid</option>
        </select>

        <button type="submit">Update Leave Record</button>
    </form>
    <hr>

    <%
        } // End of for loop
    } else {
    %>
    <p>No leave records found for this employee.</p>
    <%
    } // End of if-else
    %>
</div>

</body>
</html>
