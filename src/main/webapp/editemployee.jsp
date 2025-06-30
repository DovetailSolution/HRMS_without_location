<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.tcs.hr.AttendanceRecord" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Employee</title>
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
            max-width: 400px;
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

        .edit-container input[type="text"] {
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

        .edit-container p {
            text-align: center;
            color: #e74c3c;
        }
    </style>
</head>
<body>

<div class="edit-container">
    <h2>Edit Employee</h2>

    <%
        AttendanceRecord employee = (AttendanceRecord) request.getAttribute("employees");
        if (employee != null) {
    %>
    <form action="update" method="post">
        <input type="hidden" name="empId" value="<%= employee.getEmpId() %>">

        <label for="username">Username:</label>
        <input type="text" name="username" id="username" value="<%= employee.getUsername() %>">

        <label for="role">Role:</label>
        <input type="text" name="role" id="role" value="<%= employee.getRole() %>">

		<label>Gender:</label>
    	<input type="text" name="gender" value="<%= employee.getGender() %>"><br>

    	<label>Date of Birth:</label>
    	<input type="date" name="dob" value="<%= employee.getDob() %>"><br>

    	<label>Email:</label>
    	<input type="email" name="email" value="<%= employee.getEmail() %>"><br>

    	<label>Mobile:</label>
    	<input type="text" name="mobile" value="<%= employee.getMobile() %>"><br>

    	<label>Nationality:</label>
    	<input type="text" name="nationality" value="<%= employee.getNationality() %>"><br>

    	<label>Blood Group:</label>
    	<input type="text" name="blood" value="<%= employee.getBlood() %>"><br>
    	
        <button type="submit">Update</button>
    </form>
    <%
        } else {
    %>
    <p>Employee not found.</p>
    <%
        }
    %>
</div>

</body>
</html>
