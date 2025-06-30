<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Select Employee</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .container {
            background-color: #ffffff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            width: 400px;
            padding: 20px;
            text-align: center;
        }
        h2 {
            margin-bottom: 20px;
            color: #333;
        }
        form {
            margin: 0;
        }
        label {
            display: block;
            margin-bottom: 10px;
            font-size: 14px;
            color: #555;
        }
        select, button {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 14px;
            color: #333;
        }
        button {
            background-color: #007BFF;
            color: white;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Select Employee</h2>
        <form action="resignation" method="post">
            <label for="employee">Select an employee:</label>
            <select name="username" id="employee" required>
                <option value="" disabled selected>-- Select Employee --</option>
                <%
                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
                        ps = con.prepareStatement("SELECT empId, username FROM users");
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            String id = rs.getString("empId");
                            String name = rs.getString("username");
                %>
                            <option value="<%= name %>"><%= id + " - " + name %></option>
                <%
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (rs != null) rs.close();
                        if (ps != null) ps.close();
                        if (con != null) con.close();
                    }
                %>
            </select>

            <label for="reason">Reason for Resignation:</label>
            <select name="reason" id="reason" required>
                <option value="" disabled selected>-- Select Reason --</option>
                <option value="Personal Reasons">Personal Reasons</option>
                <option value="retired">Retired</option>
                <option value="termination">Termination</option>
                <option value="healthIssues">Health Issues</option>
                <option value="Relocation">Relocation</option>
                <option value="Work-Life Balance">Work-Life Balance</option>
                
            </select>

            <button type="submit">Submit</button>
        </form>
    </div>
</body>
</html>
