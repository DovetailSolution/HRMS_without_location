<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ page import="java.sql.Connection, java.sql.DriverManager, java.sql.PreparedStatement, java.sql.ResultSet" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manager - Assign Tasks</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 600px;
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
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #555;
        }
        .form-group input, 
        .form-group textarea, 
        .form-group select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
        }
        .form-group textarea {
            resize: vertical;
            height: 100px;
        }
        .form-group button {
            width: 100%;
            padding: 10px;
            background-color: #28a745;
            color: #fff;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
        }
        .form-group button:hover {
            background-color: #218838;
        }
        .message {
            text-align: center;
            margin-top: 10px;
            font-size: 14px;
        }
        .message.success {
            color: #28a745;
        }
        .message.error {
            color: #dc3545;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Assign Task to Employee</h2>
        <form action="manager" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label for="taskName">Task Title:</label>
                <input type="text" id="taskName" name="taskName" placeholder="Enter task title" required>
            </div>
            <div class="form-group">
                <label for="taskDescription">Task Description:</label>
                <textarea id="taskDescription" name="taskDescription" placeholder="Enter detailed task description" required></textarea>
            </div>
            <div class="form-group">
                <label for="assignedTo">Assign To (Employee):</label>
                <select id="assignedTo" name="assignedTo" required>
                    <option value="">Select Employee</option>
                    <%
                        Connection con = null;
                        PreparedStatement ps = null;
                        ResultSet rs = null;
                        String managerEmpId = (String) session.getAttribute("empid");
                        if (managerEmpId != null) {
                            try {
                                Class.forName("com.mysql.cj.jdbc.Driver");
                                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
                                String query = "SELECT empId, username FROM users WHERE reporting_manager = ?";
                                ps = con.prepareStatement(query);
                                ps.setString(1, managerEmpId);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    String empId = rs.getString("empId");
                                    String empName = rs.getString("username");
                    %>
                                    <option value="<%= empId %>"><%= empName %> (<%= empId %>)</option>
                    <%
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
                                if (ps != null) try { ps.close(); } catch (Exception e) { e.printStackTrace(); }
                                if (con != null) try { con.close(); } catch (Exception e) { e.printStackTrace(); }
                            }
                        } else {
                    %>
                            <option value="">No manager session found</option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label for="priority">Priority Level:</label>
                <select id="priority" name="priority" required>
                    <option value="Low">Low</option>
                    <option value="Medium">Medium</option>
                    <option value="High">High</option>
                    <option value="Critical">Critical</option>
                </select>
            </div>
            <div class="form-group">
                <label for="dueDate">Due Date:</label>
                <input type="date" id="dueDate" name="dueDate" required>
            </div>
            <div class="form-group">
                <label for="attachment">Attachments:</label>
                <input type="file" id="attachment" name="attachment" accept=".pdf,.docx,.xlsx,.png,.jpg">
            </div>
            <div class="form-group">
                <button type="submit">Assign Task</button>
            </div>
        </form>
        <% 
            String success = request.getParameter("success");
            String error = request.getParameter("error");
            if (success != null) {
        %>
            <div class="message success"><%= success %></div>
        <% 
            } else if (error != null) { 
        %>
            <div class="message error"><%= error %></div>
        <% 
            } 
        %>
    </div>
</body>
</html>
