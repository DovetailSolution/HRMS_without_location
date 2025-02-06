<%@ page import="java.util.List" %>
<%@ page import="com.tcs.hr.AttendanceRecord" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Salary Slip Request Status</title>
    <style>
        body {
            background-image: url("background.jpg");
            background-size: cover;
            font-family: 'Roboto', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f0f0f0;
        }
        .container {
            width: 80%;
            margin: 50px auto;
            padding: 20px;
            background-color: rgba(255, 255, 255, 0.9);
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.3);
            border-radius: 10px;
        }
        h2 {
            text-align: center;
            color: #333;
            font-size: 24px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px;
            text-align: center;
            border: 1px solid #ddd;
        }
        th {
            background-color: #007BFF;
            color: #fff;
            font-weight: bold;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Salary Slip Request Status</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Emp ID</th>
                    <th>Username</th>
                    <th>Request Date</th>
                    <th>Status</th>
                    <th>Actioned Date</th>
                    <th>Remarks</th>
                    <th>Download</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    List<AttendanceRecord> slipRequestStatus = (List<AttendanceRecord>) request.getAttribute("slipRequestStatus");
                    if (slipRequestStatus != null && !slipRequestStatus.isEmpty()) {
                        for (AttendanceRecord record : slipRequestStatus) {
                %>
                <tr>
                	<td><%= record.getId() %></td>
                    <td><%= record.getEmpId() %></td>
                    <td><%= record.getUsername() %></td>
                    <td><%= record.getRequestDate() %></td>
                    <td><%= record.getSlipstatus() %></td>
                    <td><%= record.getActionedDate() %></td>
                    <td><%= record.getRemarks() %></td>
                    <td>
                    	<form action="salaryslip" method="get">
                    		<input type="hidden" name="slipstatus" value="<%= record.getSlipstatus() %>">
                    		<input type="hidden" name="empid" value="<%= record.getEmpId() %>">
                    		<button type="submit">download</button>
                    	</form>
                    </td>
                </tr>
                <% 
                        }
                    } else { 
                %>
                <tr>
                    <td colspan="8">No salary slip requests found</td>
                </tr>
                <% 
                    }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
