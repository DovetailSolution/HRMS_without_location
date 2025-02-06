<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Onboarding - Salary Management</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
            color: #495057;
        }
        .container {
            width: 50%;
            margin: 50px auto;
            padding: 30px;
            background-color: #fff;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
        }
        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #007BFF;
            font-size: 28px;
        }
        .form-container {
            margin-bottom: 20px;
        }
        .form-container form {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }
        .form-container label {
            font-weight: bold;
            margin-bottom: 5px;
            color: #333;
        }
        .form-container input[type="text"], 
        .form-container input[type="number"], 
        .form-container input[type="date"] {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s ease;
        }
        .form-container input[type="text"]:focus, 
        .form-container input[type="number"]:focus, 
        .form-container input[type="date"]:focus {
            border-color: #007BFF;
            outline: none;
        }
        .form-container button {
            padding: 12px;
            background-color: #007BFF;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s ease;
        }
        .form-container button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Employee Boarding</h2>
        <div class="form-container">
            <form action="salary" method="post">
                <%
                    //HttpSession session = request.getSession(false);
                    String empId = (String) session.getAttribute("empid");
                    String username = (String) session.getAttribute("username");
                %>

                <label for="empId">Employee ID</label>
                <input type="text" id="empId" name="empId" value="<%= empId %>" readonly>

                <label for="name">Employee Name</label>
                <input type="text" id="name" name="name" value="<%= username %>" readonly>

                <label for="dept">Department</label>
                <input type="text" id="dept" name="dept" required>

                <label for="joining">Date of Joining</label>
                <input type="date" id="joining" name="joining" required>

                <label for="aadhar">Aadhar Number</label>
                <input type="text" id="aadhar" name="aadhar" required>

                <label for="pan">PAN Number</label>
                <input type="text" id="pan" name="pan" required>

                <label for="bank">Bank Account Number</label>
                <input type="text" id="bank" name="bankaccount" required>

                <label for="bankname">Bank Name</label>
                <input type="text" id="bankname" name="bankname" required>
                
                <label for="ifsc">IFSC Code</label>
                <input type="text" id="ifsc" name="ifsc" required>

                <label for="CTC">CTC</label>
                <input type="number" id="ctc" name="ctc" required step="0.01">

                <label for="basicsal">Basic Salary</label>
                <input type="number" id="basicsal" name="basicsal" required step="0.01">

                <label for="da">Dearness Allowance (DA)</label>
                <input type="number" id="da" name="da" required step="0.01">

                <label for="hra">House Rent Allowance (HRA)</label>
                <input type="number" id="hra" name="hra" required step="0.01">

                <label for="ta">Travelling Allowance (TA)</label>
                <input type="number" id="ta" name="ta" required step="0.01">

                <label for="ma">Medical Allowance (MA)</label>
                <input type="number" id="ma" name="ma" required step="0.01">

                <button type="submit">Submit Details</button>
            </form>
        </div>
    </div>
</body>
</html>
