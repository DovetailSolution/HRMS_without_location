<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.tcs.hr.ExpenseRecord" %>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8"> 
<title>Expense Form</title>
<style>
    /* Basic reset and styling */
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f9;
        margin: 0;
        padding: 20px;
        display: flex;
        flex-direction: column;
        align-items: center;
        min-height: 100vh;
    }
    .form-container {
        background-color: #ffffff;
        padding: 30px;
        border-radius: 10px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        width: 100%;
        max-width: 600px;
        margin: 20px;
    }
    .expense-details-container {
    background-color: #ffffff;
    padding: 30px;
    border-radius: 10px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    width: 90%; /* Takes 90% of the screen width, allowing it to expand */
    margin: 20px auto;
    overflow-x: auto; /* Horizontal scrolling for small screens */
}

    h2 {
        text-align: center;
        color: #333333;
        margin-bottom: 20px;
    }
    .form-group {
        margin-bottom: 15px;
    }
    label {
        display: block;
        font-weight: bold;
        margin-bottom: 5px;
        color: #333;
    }
    input[type="text"],
    input[type="date"],
    input[type="time"],
    input[type="file"] {
        width: 100%;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 5px;
        box-sizing: border-box;
        font-size: 14px;
    }
    button {
        background-color: #4CAF50;
        color: white;
        padding: 12px;
        border: none;
        border-radius: 5px;
        width: 100%;
        cursor: pointer;
        font-size: 16px;
        margin-top: 10px;
    }
    button:hover {
        background-color: #45a049;
    }
    .view-details-btn {
        background-color: #007bff;
        margin-top: 5px;
    }
    .view-details-btn:hover {
        background-color: #0069d9;
    }
    .expense-details-container table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }
    table th,
    table td {
        padding: 10px;
        border: 1px solid #ddd;
        text-align: left;
        font-size: 14px;
    }
    table th {
        background-color: #f2f2f2;
        color: #333;
    }
    table tr:nth-child(even) {
        background-color: #f9f9f9;
    }
    @media (max-width: 600px) {
        .form-container, .expense-details-container {
            width: 90%;
        }
        table th, table td {
            font-size: 12px;
        }
    }
</style>
</head>
<body>
    <div class="form-container">
        <h2>Expense Submission</h2>
        <form action="expense" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label for="travelRoute">Travel Route:</label>
                <input type="text" id="travelRoute" name="travelRoute" required>
            </div>
            <div class="form-group">
                <label for="date">Date of travel (from):</label>
                <input type="date" id="date" name="date" required>
            </div>
            <div class="form-group">
                <label for="timeFrom">Time from:</label>
                <input type="time" id="timeFrom" name="timeFrom" required>
            </div>
            <div class="form-group">
                <label for="timeTo">Time to:</label>
                <input type="time" id="timeTo" name="timeTo" required>
            </div>
            <div class="form-group">
                <label for="purpose">Purpose of Travel:</label>
                <input type="text" id="purpose" name="purpose" required>
            </div>
            <div class="form-group">
                <label for="project">Project Name:</label>
                <input type="text" id="project" name="projectName" required>
            </div>
            <div class="form-group">
                <label for="expensesIncurred">Expenses Incurred:</label>
                <input type="text" id="expensesIncurred" name="expensesIncurred" required>
            </div>
            <div class="form-group">
                <label for="advanceTaken">Advance Taken:</label>
                <input type="text" id="advanceTaken" name="advanceTaken" required>
            </div>
            <div class="form-group">
                <label for="mode">Mode of Travel:</label>
                <input type="text" id="mode" name="mode" required>
            </div>
            <div class="form-group">
                <label for="ticketNo">Ticket No:</label>
                <input type="text" id="ticketNo" name="ticketNo" required>
            </div>
            <div class="form-group">
                <label for="ticketDate">Ticket Date:</label>
                <input type="date" id="ticketDate" name="ticketDate" required>
            </div>
            <div class="form-group">
                <label for="attachment">Attachments:</label>
                <input type="file" id="attachment" name="attachment" accept=".pdf,.docx,.xlsx,.png,.jpg">
            </div>
            <button type="submit">Submit</button>
        </form>
        <form action="expensedetails" method="get">
            <button type="submit" class="view-details-btn">View Details</button>
        </form>
    </div>
    
    <div class="expense-details-container">
        <% 
            List<ExpenseRecord> expenseRecord = (List<ExpenseRecord>) request.getAttribute("expense");
            if (expenseRecord != null) {
        %>
        <h2>Expenses Details</h2>
        <form action="expensedetails" method="get" class="form-group">
            <label for="dateFrom">From Date:</label>
            <input type="date" id="dateFrom" name="dateFrom">
            <label for="dateTo">To Date:</label>
            <input type="date" id="dateTo" name="dateTo">
            <button type="submit" class="view-details-btn">Filter</button>
        </form>
        <table>
            <tr>
                <th>Emp Id</th>
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
                <th>Status</th>
            </tr>
            <% for (ExpenseRecord record : expenseRecord) { %>
            <tr>
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
                <td><%= record.getAttachment() %></td>
                <td><%= record.getStatus() %></td>
            </tr>
            <% } %>              
        </table>
        <% } %>
    </div>
</body>
</html>
