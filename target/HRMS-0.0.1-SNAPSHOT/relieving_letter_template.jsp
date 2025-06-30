<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Relieving Letter Template</title>
    <style>
        .container {
            width: 60%;
            margin: 40px auto;
            padding: 20px;
            background-color: #f9f9f9;
            border-radius: 10px;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
            font-family: Arial, sans-serif;
            line-height: 1.6;
        }
        .container h2 {
            text-align: center;
            margin-bottom: 20px;
        }
        .content {
            padding: 10px 20px;
            font-size: 16px;
            color: #333;
        }
        label {
            font-weight: bold;
            margin-right: 10px;
        }
        input[type="text"], input[type="date"] {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            margin-bottom: 20px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        button {
            background-color: #007BFF;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Relieving Letter</h2>
        <form action="relieving" method="post">
            <div class="content">
                <p>Date: <input type="date" name="relievingDate" required></p>
                <p>To,</p>
                <p>Mr./Mrs. <label for="employeeName">Employee Name:</label>
                	<input type="text" id="employeeName" name="employeeName" required>
                </p>
                <p>Subject: Relieving Letter</p>
                
                <p>Dear Sir/Madam,</p>
                <p>This is to certify that <label for="employeeName">Employee Name:</label>
                    <input type="text" id="employeeName" name="employeeName" required>, who was working as
                    <label for="position">Position:</label>
                    <input type="text" id="position" name="position" required>
                    in our company, has been relieved from their duties effective <span>Date of Relieving: <input type="date" name="endDate" required></span>.
                </p>
                
                <p>We appreciate their contributions during their tenure and wish them all the best in future endeavors.</p>
                
                <p>Thank you,</p>
                <p>Dovetail Solutions</p>
            </div>
            <div style="text-align: center;">
                <button type="submit">Download Relieving Letter</button>
            </div>
        </form>
    </div>
</body>
</html>
