<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tcs.hr.AttendanceRecord" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Sales Task</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            background-color: #ffffff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
            max-width: 500px;
            width: 100%;
        }
        h2 {
            color: #333333;
            text-align: center;
        }
        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
            color: #555555;
        }
        input[type="text"],
        select {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #cccccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        button {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: #ffffff;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            margin-top: 20px;
        }
        button:hover {
            background-color: #0056b3;
        }
        .form-group {
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Edit Sales Task</h2>
        
        <%
        AttendanceRecord records = (AttendanceRecord) request.getAttribute("updatesale");
        %>
        
        <form action="salestaskedit" method="post">
            <!-- Hidden field to hold task ID -->
            <input type="hidden" name="taskId" value="<%= records.getTaskid() %>">
            
            <div class="form-group">
                <label for="companyName">Company Name:</label>
                <input type="text" id="companyName" name="companyName" value="<%= records.getCompanyname() %>">
            </div>
            
            <div class="form-group">
                <label for="companyAddress">Company Address:</label>
                <input type="text" id="companyAddress" name="companyAddress" value="<%= records.getCompanyaddress() %>">
            </div>

            <div class="form-group">
                <label for="status">Status:</label>
                <select id="status" name="status">
                    <option value="inprogress" <%= "inprogress".equals(records.getSalestaskstatus()) ? "selected" : "" %>>In Progress</option>
                    <option value="completed" <%= "completed".equals(records.getSalestaskstatus()) ? "selected" : "" %>>Completed</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="meetingout">Meeting Out:</label>
                <input type="text" id="meetingout" name="meetingout" value="<%= records.getMeetingOut() %>">
            </div>

            <button type="submit">Update Status</button>
        </form>
    </div>
</body>
</html>
