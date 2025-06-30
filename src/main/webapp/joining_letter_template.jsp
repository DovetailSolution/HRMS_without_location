<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Joining Letter Template</title>
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
        <h2>Joining Letter</h2>
        <form action="joining" method="post">
            <div class="content">
                <p><label for="name">Name:</label>
                	<input type="text" id="name" name="employeeName"></p>
                <p>Address:<label for="address">Address</label>
                <input type="text" id="address" name="address">
                </p>
                <p>Dear <label for="employeename">Employee Name</label>
                <input type="text" id="name" name="employeeName">
                </p>
                <p>we are pleased to offer you, the position of <label for="postion">position</label>
                <input type="text" name="position"> with DOVETAIL SOLUTIONS. Location will be <label for="location">location</label>
                <input type="text" name="location">. You are required to join on <label for="date">Date</label>
                <input type="date" name="date">, failing which the offer may be withdrawn.</p>
                <p>In addition to the below mentioned, terms and condition included in the aggrement will also be applicable for this appointment.</p>
                <p>Terms and Condition</p>
                <p>1). Probation period: - you will be on probation for period of three months from the date of your appointment. If not confirmed, your probation period will stand extended till further notice.</p>
                <p>2). Hours of Work: The Company observes six days a week from Monday to Saturday, the timings being 9:30 AM to 6:30 PM. The hours of work are set according to business needs and may be changed when necessary.</p>
                <p>3). On the day of joining, we request you to report to HR Manager and must submit the following mentioned documents before your joining:</p>
                <p>a). Two passport size photograph (colored)</p>
                <p>b). Last salary slip & Relieving letter/experience certificate/resignation acceptance from previous employer/reference contact number.</p>
                <p>c). Scanned copy of all educational documents.</p>
               	<p>d). Proof of residence and date of birth.</p>
               	<p>e). Copy of PAN card, Aadhar card.</p>
               	<p>The offer is contingent upon positive reference check and submission all the documents listed in this letter.</p>
               	<p>We Welcome you as a Member of our team and wish you a successful career with the organization.</p> 
            </div>
            <div style="text-align: center;">
                <button type="submit">Download Joining Letter</button>
            </div>
        </form>
    </div>
</body>
</html>
