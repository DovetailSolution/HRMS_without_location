<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ page import="java.sql.Connection, java.sql.DriverManager, java.sql.PreparedStatement, java.sql.ResultSet" %>
<%@page import="com.tcs.hr.AttendanceRecord" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manager Panel</title>
    <style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        margin: 0;
        padding: 0;
        display: flex;
        height: 100vh;
    }

    /* Sidebar styling */
    .sidebar {
        width: 200px;
        background-color: #333;
        color: #fff;
        padding-top: 20px;
    }

    .sidebar button {
        width: 100%;
        padding: 15px;
        background: #333;
        border: none;
        color: #fff;
        text-align: left;
        cursor: pointer;
        font-size: 16px;
    }

    .sidebar button:hover {
        background-color: #575757;
    }

    .sidebar button.active {
        background-color: #575757;
    }

    .content {
        flex-grow: 1;
        padding: 20px;
        background: #f4f4f4;
    }

    .panel {
        display: none;
    }

    .panel.active {
        display: block;
    }

    /* Form styling */
    .form-group {
        margin-bottom: 20px;
    }

    label {
        font-weight: bold;
        margin-bottom: 5px;
        display: block;
    }

    input[type="text"],
    input[type="date"],
    textarea,
    select,
    input[type="file"] {
        width: 100%;
        padding: 10px;
        font-size: 16px;
        border: 1px solid #ccc;
        border-radius: 5px;
        box-sizing: border-box;
    }

    textarea {
        resize: vertical;
    }

    button[type="submit"] {
        padding: 10px 20px;
        font-size: 16px;
        background-color: #333;
        color: #fff;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }

    button[type="submit"]:hover {
        background-color: #575757;
    }
</style>
    <script>
        function showPanel(panelId) {
            // Hide all panels
            var panels = document.querySelectorAll('.panel');
            panels.forEach(panel => panel.classList.remove('active'));

            // Show the selected panel
            document.getElementById(panelId).classList.add('active');

            // Highlight the active button
            var buttons = document.querySelectorAll('.sidebar button');
            buttons.forEach(button => button.classList.remove('active'));
            document.querySelector(`[data-panel="${panelId}"]`).classList.add('active');
        }
    </script>
</head>
<body>
    <div class="sidebar">
    	<button data-panel="dashboard" onclick="showPanel('dashboard')">Dashboard</button>
        <button data-panel="assignTaskPanel" onclick="showPanel('assignTaskPanel')">Assign Task</button>
        <button data-panel="viewTasksPanel" onclick="showPanel('viewTasksPanel')">View Tasks</button>
        <button data-panel="manageEmployeesPanel" onclick="showPanel('manageEmployeesPanel')">Manage Employees</button>
    </div>

    <div class="content">
    
    	 <!-- View Tasks Panel -->
        <div id="dashboard" class="panel">
            <h2>Dashboard</h2>
            <!-- You can add the code for viewing tasks here -->
            <p>Dashboard will be showing soon......</p>
        </div>
    
        <!-- Assign Task Panel -->
        <div id="assignTaskPanel" class="panel active">
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
                    // Fetching employees list dynamically based on manager ID
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
                        } 
                        catch (Exception e) 
                        {
                            e.printStackTrace();
                        }
                        finally 
                        {
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
</div>

       <!-- View Tasks Panel -->
	<div id="viewTasksPanel" class="panel">
    <h2>View Assigned Tasks</h2>
    <%
        //Connection con = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        List<AttendanceRecord> records = new ArrayList<>();
        try {
            String empid = (String) session.getAttribute("empid");
            String username=(String) session.getAttribute("user");
            //String managerid=(String) session.getAttribute("managerId");
            
            if (managerEmpId != null) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
                String query = "SELECT * FROM tasks";
                ps1 = con.prepareStatement(query);
                //ps1.setString(1, managerid);
                rs1 = ps1.executeQuery();

                while (rs1.next()) {
                    AttendanceRecord record = new AttendanceRecord();
                    record.setTaskName(rs1.getString("task_name"));
                    record.setTaskDescription(rs1.getString("task_description"));
                    record.setAssignedTo(rs1.getString("assigned_to"));
                    record.setTaskUsername(rs1.getString("username"));
                    record.setStatus(rs1.getString("status"));

                    records.add(record);
                }
            } else {
                out.println("<p>No manager session found.</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs1 != null) try { rs1.close(); } catch (Exception e) { e.printStackTrace(); }
            if (ps1 != null) try { ps1.close(); } catch (Exception e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (Exception e) { e.printStackTrace(); }
        }
        request.setAttribute("tasks", records);
    %>

    <%
        List<AttendanceRecord> tasks = (List<AttendanceRecord>) request.getAttribute("tasks");
        if (tasks != null && !tasks.isEmpty()) {
    %>
        <table border="1" cellpadding="5">
            <tr>
                <th>Task Name</th>
                <th>Task Description</th>
                <th>Assigned To</th>
                <th>Username</th>
                <th>Status</th>
            </tr>
            <%
                for (AttendanceRecord record : tasks) {
            %>
                <tr>
                    <td><%= record.getTaskName() %></td>
                    <td><%= record.getTaskDescription() %></td>
                    <td><%= record.getAssignedTo() %></td>
                    <td><%= record.getTaskUsername() %></td>
                    <td><%= record.getStatus() %></td>
                </tr>
            <%
                }
            %>
        </table>
    <%
        } else {
            out.println("<p>No tasks assigned yet.</p>");
        }
    %>
</div>


        <!-- Manage Employees Panel -->
        <div id="manageEmployeesPanel" class="panel">
            <h2>Manage Employees</h2>
            <!-- You can add the code for managing employees here -->
            <p>Employee management functionality goes here.</p>
        </div>
    </div>
</body>
</html>
