<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Tasks</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 100%;
            margin: 0 auto;
            background: #fff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }
        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f4f4f4;
        }
        .no-tasks {
            text-align: center;
            color: #888;
        }
        .form-group {
            margin-top: 20px;
        }
        .form-group button {
            background-color: #28a745;
            color: #fff;
            border: none;
            padding: 10px;
            cursor: pointer;
            border-radius: 5px;
            width: 100%;
        }
        .form-group button:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Your Assigned Tasks</h2>
        <%
            List<Map<String, Object>> tasks = (List<Map<String, Object>>) request.getAttribute("tasks");
            if (tasks == null || tasks.isEmpty()) {
        %>
            <p class="no-tasks">No tasks assigned to you.</p>
        <% } else { %>
            <table>
                <thead>
                    <tr>
                        <th>Task Name</th>
                        <th>Description</th>
                        <th>Due Date</th>
                        <th>Priority</th>
                        <th>Status</th>
                        <th>Actions</th>
                        <th>Sent by Manager</th>
                        <th>Attachment</th>
                        <th>Remarks</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Map<String, Object> task : tasks) {
                    %>
                        <tr>
                            <td><%= task.get("taskName") %></td>
                            <td><%= task.get("description") %></td>
                            <td><%= task.get("dueDate") %></td>
                            <td><%= task.get("priority") %></td>
                            <td><%= task.get("status") %></td>
                            <td>
                                <form action="taskDetails" method="post" style="display:inline;">
                                    <input type="hidden" name="taskId" value="<%= task.get("taskId") %>">
                                    <button type="submit">View Details</button>
                                </form>
                                <form action="updateTaskStatus" method="post" style="display:inline;">
                                    <input type="hidden" name="taskId" value="<%= task.get("taskId") %>">
                                    <select name="status">
                                        <option value="In Progress" <%= "In Progress".equals(task.get("status")) ? "selected" : "" %>>In Progress</option>
                                        <option value="Completed" <%= "Completed".equals(task.get("status")) ? "selected" : "" %>>Completed</option>
                                    </select>
                                    <button type="submit">Update</button>
                                </form>
                            </td>
                            <td>
    <% 
        String attachmentPath = (String) task.get("attachmentPath");
        if (attachmentPath != null && !attachmentPath.isEmpty()) { 
    %>
        <form action="downloadAttachment" method="get">
            <input type="hidden" name="taskId" value="<%= task.get("taskId") %>">
            <button type="submit">Download Attachment</button>
        </form>
    <% }  %>
</td>

          <td>
          <input type="file" id="attachment" name="attachment" accept=".pdf,.docx,.xlsx,.png,.jpg">
          </td>
          <td>
		    <form action="task" method="post">
		        <label for="remark-<%= task.get("taskId") %>">Remarks:</label>
		        <input type="hidden" name="taskId" value="<%= task.get("taskId") %>">
		        <input type="text" id="remark-<%= task.get("taskId") %>" name="remark" placeholder="Add your remark">
		        <button type="submit">Submit</button>
			</form>		       
		</td>

                            
            </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        <% } %>
    </div>
</body>
</html>
