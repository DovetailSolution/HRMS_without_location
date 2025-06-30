<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tcs.hr.ExpenseRecord" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Expenses Details</title>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        margin: 0;
        padding: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        min-height: 100vh;
    }
    .container {
        width: 90%;
        max-width: 1200px;
        background-color: #ffffff;
        padding: 20px;
        margin-top: 30px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        border-radius: 8px;
    }
    h2 {
        text-align: center;
        color: #333333;
        margin-bottom: 20px;
    }
    table {
        width: 100%;
        border-collapse: collapse;
        margin-bottom: 20px;
    }
    th, td {
        padding: 12px;
        border: 1px solid #dddddd;
        text-align: left;
    }
    th {
        background-color: #f2f2f2;
        font-weight: bold;
    }
    tr:nth-child(even) {
        background-color: #f9f9f9;
    }
    button {
        padding: 8px 12px;
        font-size: 14px;
        margin: 5px 0;
        width: 48%;
        background-color: #4CAF50;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }
    button[name="action"][value="reject"] {
        background-color: #e74c3c;
    }
    button:hover {
        opacity: 0.9;
    }
    .attachment-link {
        color: #007BFF;
        text-decoration: none;
        font-weight: bold;
    }
    .attachment-link:hover {
        text-decoration: underline;
    }
</style>
</head>
<body>
    <div class="container">
        <%
            List<ExpenseRecord> expenses = (List<ExpenseRecord>) request.getAttribute("expensesdetails");
            if (expenses != null) {
        %>
            <h2>Expenses Details</h2>
            <table>
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
                <%
                    for (ExpenseRecord record : expenses) {
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
                        <a href="<%= record.getAttachment() %>" target="_blank" class="attachment-link">View</a>
                    </td>
                    <td>
                        <form action="accdashboard" method="post" style="display: flex; gap: 5px;">
                            <input type="hidden" name="empId" value="<%= record.getEmpid() %>">
                            <input type="hidden" name="expenseId" value="<%= record.getId() %>">
                            <button type="submit" name="action" value="issued">Issued</button>
                            <button type="submit" name="action" value="reject">Reject</button>
                        </form>
                    </td>
                </tr>
                <%
                    }
                %>
            </table>
        <%
            }
        %>
    </div>
</body>
</html>
