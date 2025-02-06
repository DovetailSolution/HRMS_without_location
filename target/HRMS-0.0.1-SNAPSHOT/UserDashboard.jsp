<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Dashboard</title>
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
    </style>
    <script>
        function loadUserAction(action) {
            const contentDiv = document.querySelector('.content');
            fetch(action + ".jsp")
                .then(response => response.text())
                .then(data => {
                    contentDiv.innerHTML = data;
                })
                .catch(err => {
                    console.error('Error loading page:', err);
                });
        }

        function getLocationAndSubmit(action) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    document.getElementById("latitude").value = position.coords.latitude;
                    document.getElementById("longitude").value = position.coords.longitude;
                    document.getElementById("loginAction").value = action;
                    document.getElementById("loginForm").submit();
                },
                (error) => {
                    console.error("Error fetching location:", error);
                    alert("Location access is required for this action.");
                }
            );
        }
    </script>
</head>
<body>
    <div class="sidebar">
        <h2>User Dashboard</h2>
        <button onclick="loadUserAction('userProfile')">My Profile</button>
        <button onclick="loadUserAction('attendance')">Attendance</button>
        <button onclick="loadUserAction('leave')">Leave Requests</button>
        <button onclick="loadUserAction('salarySlip')">Salary Slip</button>
    </div>
    <div class="content">
        <div class="container">
    <h2>Welcome, <%= request.getSession().getAttribute("user") %></h2>
    <div id="currentDateTime">
        <div id="currentDate"></div>
        <div id="currentTime"></div>
    </div>
    <%
        Boolean signedIn = (Boolean) request.getSession().getAttribute("signedIn");
        java.sql.Timestamp lastSignInTime = (java.sql.Timestamp) request.getSession().getAttribute("lastSignInTime");
        java.sql.Timestamp lastSignOutTime = (java.sql.Timestamp) request.getSession().getAttribute("lastSignOutTime");
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

    <form action="attendance" method="post">
        <input type="hidden" name="action" value="viewstatus">
        <button type="submit">View Status</button>
    </form>

    <form action="attendance" method="post">
        <input type="hidden" name="action" value="downloadReport"><br>
        <label for="yearMonth">Select Month:</label>
        <input type="month" id="yearMonth" name="yearMonth" required>
        <button type="submit">Download Monthly Report</button>
    </form>
</div>
        
        
    </div>
</body>
</html>
