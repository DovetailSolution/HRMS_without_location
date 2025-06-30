<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.tcs.hr.AttendanceRecord" %>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
    <title>Salary Slip</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .salary-slip {
            width: 600px;
            margin: 20px auto;
            padding: 20px;
            border: 1px solid #ddd;
            box-shadow: 0px 0px 10px rgba(0,0,0,0.1);
        }
        .header, .details, .salary-details, .footer {
            margin-bottom: 20px;
        }
        .header h2 {
            text-align: center;
            margin: 0;
            font-size: 24px;
        }
        .details, .salary-details {
            width: 100%;
            border-collapse: collapse;
        }
        .details th, .details td, .salary-details th, .salary-details td {
            padding: 8px;
            border: 1px solid #ddd;
            text-align: left;
        }
        .details th {
            background-color: #f4f4f4;
        }
        .footer {
            text-align: center;
        }
    </style>
</head>
<body>

<%  
    // Retrieve the list of records from the request attribute
    List<AttendanceRecord> records = (List<AttendanceRecord>) request.getAttribute("records");
    AttendanceRecord record = records != null && !records.isEmpty() ? records.get(0) : null;

    // Calculate total salary if record is not null
    double totalSalary = 0;
    if (record != null) {
        totalSalary = record.getBasicSalary() + record.getDa() + record.getHra() + record.getMa() + record.getTa();
    }
%>

<div class="salary-slip">
    <div class="header">
        <h2>Salary Slip</h2>
        <p><strong>Month:</strong> October 2024</p>
    </div>

    <table class="details">
        <tr>
            <th>Employee ID</th>
            <td><%= record != null ? record.getEmpId() : "" %></td>
        </tr>
        <tr>
            <th>Name</th>
            <td><%= record != null ? record.getUsername() : "" %></td>
        </tr>
        <tr>
            <th>Role</th>
            <td><%= record != null ? record.getRole() : "" %></td>
        </tr>
        <tr>
            <th>Department</th>
            <td><%= record != null ? record.getDepartment() : "" %></td>
        </tr>
        <tr>
            <th>Bank Name</th>
            <td><%= record != null ? record.getBankname() : "" %></td>
        </tr>
        <tr>
            <th>Bank Account No</th>
            <td><%= record != null ? record.getBankaccountno() : "" %></td>
        </tr>
    </table>

    <table class="salary-details">
        <tr>
            <th>Description</th>
            <th>Amount (₹)</th>
        </tr>
        <tr>
            <td>Basic Salary</td>
            <td><%= record != null ? record.getBasicSalary() : 0 %></td>
        </tr>
        <tr>
            <td>Dearness Allowance (DA)</td>
            <td><%= record != null ? record.getDa() : 0 %></td>
        </tr>
        <tr>
            <td>House Rent Allowance (HRA)</td>
            <td><%= record != null ? record.getHra() : 0 %></td>
        </tr>
        <tr>
            <td>Medical Allowance (MA)</td>
            <td><%= record != null ? record.getMa() : 0 %></td>
        </tr>
        <tr>
            <td>Travelling Allowance (TA)</td>
            <td><%= record != null ? record.getTa() : 0 %></td>
        </tr>
        <tr>
            <th>Total</th>
            <th><%= totalSalary %></th>
        </tr>
    </table>

   <div class="footer">
    <form action="salaryslip" method="get">
        <input type="hidden" name="slipstatus" value="Approved">
        <input type="hidden" name="download" value="pdf">
        <button type="submit">Download Salary Slip as PDF</button>
    </form>
</div>

</div>

</body>
</html>
