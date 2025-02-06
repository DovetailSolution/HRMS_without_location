<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.sql.Timestamp, java.util.List, com.tcs.hr.AttendanceRecord" %>
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
        <h2>Attendance Status</h2>
        <table>
            <tr>
                <th>Sign In Time</th>
                <th>Sign-In Location</th>
                <th>Sign Out Time</th>
                <th>Sign-Out Location</th>
            </tr>
            <%
                List<AttendanceRecord> records = (List<AttendanceRecord>) request.getAttribute("attendanceRecords");
                if (records != null && !records.isEmpty()) {
                    for (AttendanceRecord record : records) {
            %>
                        <tr>
                            <td><%= record.getSignInTime() %></td>
                            <td><%= record.getLocation() %></td>
                            <td><%= record.getSignOutTime() %></td>
                            <td><%= record.getSignOutLocation() %></td>
                        </tr>
            <%
                    }
                } else {
            %>
                <tr>
                    <td colspan="4" style="text-align: center;">No records found</td>
                </tr>
            <%
                }
            %>
        </table>
        <p>Total Daily Time: <strong><%= request.getAttribute("totalDailyTime") %></strong></p>
        <p>Total Monthly Time: <strong><%= request.getAttribute("totalMonthlyTime") %></strong></p>
    </div>
</body>
</html>
