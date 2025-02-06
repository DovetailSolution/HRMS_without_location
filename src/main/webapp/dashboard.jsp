<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tcs.hr.AttendanceRecord" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <style>
        body {
            background-image: url("user2.png");
            background-size: cover;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
        }
        .sidebar {
            width: 250px;
            height: 100vh;
            position: fixed;
            top: 0;
            left: 0;
            background-color: #2c3e50;
            color: white;
            padding: 20px 15px;
            box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
        }
        .sidebar h2 {
            text-align: center;
            font-size: 24px;
            margin-bottom: 30px;
        }
        .sidebar a {
            display: block;
            color: white;
            text-decoration: none;
            margin: 10px 0;
            padding: 10px;
            border-radius: 4px;
            text-align: center;
        }
        .sidebar a:hover {
            background-color: #34495e;
        }
        .container {
            max-width: 400px;
            margin: 50px auto;
            margin-right: 350px;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            text-align: center;
        }
        table {
            width: 100%;
            margin-bottom: 20px;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        button {
            padding: 10px;
            font-size: 16px;
            margin-top: 15px;
            width: 100%;
        }
        #currentDateTime {
            text-align: center;
            margin-top: 20px;
        }
        .container a {
		    display: block;
		    padding: 10px;
		    font-size: 16px;
		    text-align: center;
		    background-color: #EFEFEF; /* Matches the Sign In button */
		    color: black; /* Adjusted to black for better readability */
		    text-decoration: none;
		    border-radius: 5px;
		    margin-top: 15px;
		    width: 100%;
		    box-sizing: border-box;
		    border: 1px solid #000000; /* Updated border to match the button */
    		box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1); /* Added subtle shadow for outline effect */
		}

		.container a:hover {
		    background-color: #EFEFEF;
}

    </style>
    <script>
        	function getLocationAndSubmit(action) {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(function(position) {
                    document.getElementById('latitude').value = position.coords.latitude;
                    document.getElementById('longitude').value = position.coords.longitude;
                    document.getElementById('loginAction').value = action;
                    document.getElementById('loginForm').submit();
                }, function(error) {
                    alert('Error fetching location: ' + error.message);
                });
            } else {
                alert('Geolocation is not supported by this browser.');
            }
        } 

        function updateTime() {
            const now = new Date();
            const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
            const currentDate = now.toLocaleDateString(undefined, options);
            const currentTime = now.toLocaleTimeString();

            document.getElementById('currentDate').innerHTML = currentDate;
            document.getElementById('currentTime').innerHTML = currentTime;
        }

        setInterval(updateTime, 1000);
        window.onload = updateTime;
    </script>
</head>
<body>
    <div class="container">
        <h2>Welcome, <%= request.getSession().getAttribute("user") %></h2>
        <div id="currentDateTime">
            <div id="currentDate"></div>
            <div id="currentTime"></div>
        </div>
        <%
            Boolean signedIn = (Boolean) request.getSession().getAttribute("signedIn");
            java.sql.Timestamp lastSignInTime = (java.sql.Timestamp) request.getSession().getAttribute("lastSignInTime");
            java.sql.Timestamp lastSignOutTime=(java.sql.Timestamp) request.getSession().getAttribute("lastSignOutTime");
            //String lastSignOutTime = (String) request.getSession().getAttribute("lastSignOutTime");
            String location = (String) request.getSession().getAttribute("location");
        %>
        <table>
            <tr>
                <th>Sign In Time</th>
                <td><%= lastSignInTime != null ? lastSignInTime : "Not Signed In" %></td>
            </tr>
            <tr>
                <th>Sign Out Time</th>
                <td><%= lastSignOutTime != null ? lastSignOutTime : "Not Signed Out" %></td>
            </tr>
            <tr>
                <th>Location</th>
                <td><%= location != null ? location : "Unknown" %></td>
            </tr>
        </table>
 <!------------------------------------------------------------------------------------------------->       
        <form id="loginForm" action="attendance" method="post">
            <input type="hidden" name="latitude" id="latitude">
            <input type="hidden" name="longitude" id="longitude">
            <input type="hidden" name="action" id="loginAction">
            <%
                if (signedIn == null || !signedIn) {
            %>
                <button type="button" onclick="getLocationAndSubmit('signin')">Sign In</button>
            <% 
                } else {
            %>
                <button type="button" onclick="getLocationAndSubmit('signout')">Sign Out</button>
            <%
                }
            %>
        </form>

<!------------------------------------------------------------------------------------------------------>
        <form action="attendance" method="post">
            <input type="hidden" name="action" value="viewstatus">
            <button type="submit">View Status</button>
        </form>

<!---------------------------------------------------------------------------------------------------->
        <form action="attendance" method="post">
            <input type="hidden" name="action" value="downloadReport"><br>
            <label for="yearMonth">Select Month:</label>
            <input type="month" id="yearMonth" name="yearMonth" required>
            <button type="submit">Download Monthly Report</button>
        </form>
<!------------------------------------------------------------------------------------------------->     
        <form action="leave" method="post">
            <label for="leaveType">Select Leave Type:</label>
            <select id="leaveType" name="leaveType">
                <option value="Vacation">Vacation</option>
                <option value="Sick Leave">Sick Leave</option>
                <option value="Casual Leave">Casual Leave</option>
                <option value="Half Day">Half Day</option>
                <option value="Work from Home">Work from Home</option>
                <option value="Short Leave">Short Leave</option>
            </select>
            <br><br>
            <label for="startDate">Start Date:</label>
            <input type="date" id="startDate" name="startDate" required>
            <br><br>
            <label for="endDate">End Date:</label>
            <input type="date" id="endDate" name="endDate" required>
            <br><br>
            <label for="leaveRemarks">Remarks:</label>
            <input type="text" id="leaveRemarks" name="leaveRemarks">
            <br><br>
            <button type="submit" name="action" value="submit">Submit Leave Request</button>
        </form>
<!----------------------------------------------------------------------------------------------------->      
        <form action="leave" method="post">
            <button type="submit" name="action" value="status">Leave Status</button>
        </form>
<!-- ------------------------------------------------------------------------------------------------ -->
        <form action="task" method="get">
        	<button type="submit">Task</button>
        </form>
<!------------------------------------------------------------------------------------------------------->       
        <form action=salarySlipRequest method="get">
        	<label for="month">Salary slip for:</label>
        	<input type="month" name="month">
    		<input type="hidden" name="action" value="requestSalarySlip">
    		<button type="submit">Request Salary Slip</button>
		</form>
<!---------------------------------------------------------------------------------------------------->
		<form action="salarySlipRequest" method="get">
			<input type="hidden" name="action" value="slipstatus">
			<button type="submit">Salary Slip Status</button>
		</form>
<!------------------------------------------------------------------------------------------------------->
    	<form action="notice" method="get">
    		<button type="submit">Urgent Notice</button>
    	</form>
 <!-- ---------------------------------------------------------------------------------------------------->
    	<a href="reminder.jsp">Set reminder</a>
 <!-- --------------------------------------------------------------------------------------------------- -->
    </div>
</body>
</html>
