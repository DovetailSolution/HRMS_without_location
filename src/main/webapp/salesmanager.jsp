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
    </style>
    <script>
        function showPanel(panelId) {
            var panels = document.querySelectorAll('.panel');
            panels.forEach(panel => panel.classList.remove('active'));

            document.getElementById(panelId).classList.add('active');

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
        <!-- Dashboard Panel -->
        <div id="dashboard" class="panel">
            <h2>Dashboard</h2>
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
        </div>

        <!-- View Tasks Panel -->
        <div id="viewTasksPanel" class="panel">
            <h2>View Assigned Tasks</h2>
            <%
                PreparedStatement ps1 = null;
                ResultSet rs1 = null;
                List<AttendanceRecord> records = new ArrayList<>();
                try {
                    String empid = (String) session.getAttribute("empid");
                    if (empid != null) {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
                        String query = "SELECT * FROM client";
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();

                        while (rs1.next()) {
                            AttendanceRecord record = new AttendanceRecord();
                            record.setUsername(rs1.getString("username"));
                            record.setSaledate(rs1.getDate("date"));
                            record.setCompanyname(rs1.getString("company_name"));
                            record.setCompanyaddress(rs1.getString("company_address"));
                            record.setClient(rs1.getString("client_name"));
                            record.setClientdesignation(rs1.getString("designation"));
                            record.setClientmobile(rs1.getString("mobile"));
                            record.setClientemail(rs1.getString("email"));
                            record.setWork(rs1.getString("work"));
                            record.setClientamount(rs1.getInt("Amount_received"));
                            record.setMeetingIn(rs1.getString("meeting_in"));
                            record.setMeetingOut(rs1.getString("meeting_out"));

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
            %>
            <%
                if (!records.isEmpty()) {
            %>
                <table border="1" cellpadding="5">
                    <tr>
                    	<th>Username</th>
                    	<th>Date</th>
                        <th>Company Name</th>
                        <th>Company Address</th>
                        <th>Client Name</th>
                        <th>Designation</th>
                        <th>Mobile</th>
                        <th>Email</th>
                        <th>Work</th>
                        <th>Amount received</th>
                        <th>Meeting In</th>
                        <th>Meeting Out</th>
                    </tr>
                    <%
                        for (AttendanceRecord record : records) {
                    %>
                        <tr>
                        	<td><%= record.getUsername() %></td>
                        	<td><%= record.getSaledate() %></td>
                            <td><%= record.getCompanyname() %></td>
                            <td><%= record.getCompanyaddress() %></td>
                            <td><%= record.getClient() %></td>
                            <td><%= record.getClientdesignation() %></td>
                            <td><%= record.getClientmobile() %></td>
                            <td><%= record.getClientemail() %></td>
                            <td><%= record.getWork() %></td>
                            <td><%= record.getClientamount() %></td>
                            <td><%= record.getMeetingIn() %></td>
                            <td><%= record.getMeetingOut() %></td>
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
            <p>Employee management functionality goes here.</p>
        </div>
    </div>
</body>
</html>
