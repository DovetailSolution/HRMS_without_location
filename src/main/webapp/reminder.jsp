<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Set Reminder</title>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f9;
        margin: 0;
        padding: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
    }
    .form-container {
        background-color: #ffffff;
        padding: 20px 30px;
        border-radius: 10px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        width: 100%;
        max-width: 500px;
    }
    .form-container h1 {
        text-align: center;
        margin-bottom: 20px;
        color: #333;
    }
    .form-container label {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
        color: #555;
    }
    .form-container input {
        width: 100%;
        padding: 10px;
        margin-bottom: 15px;
        border: 1px solid #ccc;
        border-radius: 5px;
        box-sizing: border-box;
        font-size: 14px;
    }
    .form-container button {
        width: 100%;
        padding: 10px;
        background-color: #007BFF;
        color: white;
        border: none;
        border-radius: 5px;
        font-size: 16px;
        cursor: pointer;
        margin-bottom: 10px;
    }
    .form-container button:hover {
        background-color: #0056b3;
    }
    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 10px;
    }
    table th, table td {
        border: 1px solid #ccc;
        padding: 8px;
        text-align: center;
    }
    table th {
        background-color: #007BFF;
        color: white;
    }
</style>
</head>
<body>

    <!-- Form to Set Reminder and View Details -->
    <div class="form-container">
        <h1>Set Reminder</h1>
        <form action="reminder" method="post">
            <label for="date">Date</label>
            <input type="date" name="date" id="date" required>

            <label for="time">Time</label>
            <input type="time" name="time" id="time" required>

            <label for="event">Event</label>
            <input type="text" name="event" id="event" required>

            <button type="submit">Set Reminder</button>
        </form>

        <form action="reminder" method="get">
            <button type="submit">View Details</button>
        </form>

        <%-- Show the table only if reminders are available --%>
        <%
            java.util.List<java.util.Map<String, String>> reminders =
                    (java.util.List<java.util.Map<String, String>>) request.getAttribute("reminders");
            if (reminders != null) {
        %>
        <table>
            <tr>
                <th>Date</th>
                <th>Time</th>
                <th>Event</th>
            </tr>
            <% for (java.util.Map<String, String> reminder : reminders) { %>
            <tr>
                <td><%= reminder.get("date") %></td>
                <td><%= reminder.get("time") %></td>
                <td><%= reminder.get("event") %></td>
            </tr>
            <% } %>
        </table>
        <% } %>
    </div>

</body>
</html>
