<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.sql.Connection, java.sql.DriverManager, java.sql.PreparedStatement, java.sql.ResultSet" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f0f0f0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .container {
            width: 100%;
            max-width: 400px;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            margin: 20px;
        }
        h2 {
            text-align: center;
            color: #333;
        }
        form {
            display: flex;
            flex-direction: column;
        }
        label {
            margin-bottom: 5px;
            color: #333;
        }
        input, select {
            margin-bottom: 15px;
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 4px;
            width: 100%;
        }
        button {
            padding: 10px;
            font-size: 16px;
            background-color: #4CAF50;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s;
            width: 100%;
        }
        label {
            margin-bottom: 5px;
            font-size: 14px;
            color: #333;
        }
        button:hover {
            background-color: #45a049;
        }
        .message {
            text-align: center;
            margin-top: 20px;
            padding: 10px;
            border-radius: 4px;
        }
        .error {
            color: #ff0000;
        }
        .success {
            color: #4CAF50;
        }
        
        /* Media query for smaller screens */
        @media (max-width: 600px) {
            .container {
                padding: 15px;
            }
            input, button {
                padding: 8px;
                font-size: 14px;
            }
            h2 {
                font-size: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Register</h2>
        <form action="register" method="post">
            <label for="emp_id">Employee ID:</label>
            <input type="text" id="empId" name="empId" required>
            
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
            
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
            
            <label for="role">Role:</label>
            <input type="text" id="role" name="role" required>
            
            <label for="gender">Gender:</label>
            <input type="text" id="gender" name="gender" required>
            
            <label for="DOB">Date of Birth:</label>
            <input type="date" id="DOB" name="DOB" required>
            
            <label for="emailid">Email Id:</label>
            <input type="email" id="email" name="email" required>
            
            <label for="mobile">Mobile No:</label>
            <input type="tel" id="mobile" name="mobile" required>
            
            <label for="nationality">Nationality:</label>
            <input type="text" id="nationality" name="nationality" required>
            
            <label for="bg">Blood Group:</label>
            <input type="text" id="blood" name="blood" required>
            
            <label for="managers">Reporting Manager:</label>
            <select id="managers" name="managerEmpId" >
                <option value="">Select Manager</option>
                <%
                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
                        String query = "SELECT empId, username FROM users WHERE role IN ('manager', 'salesmanager')";

                        ps = con.prepareStatement(query);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            String managerEmpId = rs.getString("empId");
                            String managerName = rs.getString("username");
                %>
                            <option value="<%= managerEmpId %>"><%= managerName %></option>
                <%
                        }
                    } 
                    catch (Exception e) 
                    {
                        e.printStackTrace();
                    } 
                    finally 
                    {
                        if (rs != null) try { rs.close(); } catch (Exception e) { }
                        if (ps != null) try { ps.close(); } catch (Exception e) { }
                        if (con != null) try { con.close(); } catch (Exception e) { }
                    }
                %>
            </select>
            
            <button type="submit">Next</button>
        </form>
        <div class="message error">
            <%
                String error = request.getParameter("error");
                if (error != null) {
                    out.println(error);
                }
            %>
        </div>
        <div class="message success">
            <%
                String success = request.getParameter("success");
                if (success != null) {
                    out.println(success);
                }
            %>
        </div>
    </div>
</body>
</html>
