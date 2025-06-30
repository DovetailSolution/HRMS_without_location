<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <style>
        body {
        
    background-image: url("login6.jpg");
    background-size: cover;
    background-repeat: no-repeat;
    font-family: Arial, sans-serif;
    background-color: #f0f0f0;
    margin: 0;
    padding: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
           
        }
        .login-container {
            width: 300px;
            margin: 100px auto;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }
        h2 {
            text-align: center;
            margin-bottom: 20px;
        }
        form {
            display: flex;
            flex-direction: column;
        }
        input {
            margin-bottom: 10px;
            padding: 10px;
            font-size: 16px;
        }
        button {
            padding: 10px;
            background-color: #007BFF;
            color: #fff;
            border: none;
            cursor: pointer;
        }
        .message.error {
            color: red;
            text-align: center;
            margin-bottom: 10px;
        }
    </style>
    <script>
        function getLocationAndSubmit() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(function(position) {
                    document.getElementById('latitude').value = position.coords.latitude;
                    document.getElementById('longitude').value = position.coords.longitude;
                    document.getElementById('loginForm').submit();
                }, function(error) {
                    alert('Error fetching location: ' + error.message);
                });
            } else {
                alert('Geolocation is not supported by this browser.');
            }
        }
    </script>
</head>
<body>
    <div class="login-container">
        <h2>Login</h2>
        <div class="message error">
            <%
                String error = request.getParameter("error");
                if (error != null) {
                    out.println(error);
                }
            %>
        </div>
        <form id="loginForm" action="login" method="post">
            <input type="text" name="username" placeholder="Username" required>
            <input type="password" name="password" placeholder="Password" required>
            <input type="hidden" name="latitude" id="latitude">
            <input type="hidden" name="longitude" id="longitude">
            <button type="button" onclick="getLocationAndSubmit()">Login</button>
        </form>
    </div>
</body>
</html>
