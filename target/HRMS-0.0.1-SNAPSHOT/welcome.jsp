<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to HRMS</title>
    <style>
        body {
    background-image: url("welcome2.jpg");
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
.container {
	background-color: rgba(255, 255, 255);
	margin-bottom:200px;
    text-align: center;
    padding: 40px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
}
h1 {
    margin-bottom: 20px;
    color: #007BFF;
}
.login-button {
    padding: 12px 24px;
    background-color: #007BFF;
    color: #fff;
    text-decoration: none;
    border-radius: 5px;
    font-size: 16px;
    border: none;
    cursor: pointer;
    transition: background-color 0.3s ease;
}
.login-button:hover {
    background-color: #0056b3;
}

    </style>
</head>
<body>
    <div class="container">
        <h1>Welcome to HRMS</h1>
        <a href="login.jsp" class="login-button">Login</a>
    </div>
</body>
</html>
