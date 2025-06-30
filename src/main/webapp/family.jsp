<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Family Details Form</title>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        margin: 0;
        padding: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
    }
    form {
        background-color: #fff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        width: 300px;
    }
    label {
        display: block;
        margin-bottom: 8px;
        font-weight: bold;
    }
    input[type="text"],
    input[type="date"] {
        width: 100%;
        padding: 8px;
        margin-bottom: 12px;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box;
    }
    button {
        width: 100%;
        background-color: #4CAF50;
        color: white;
        padding: 10px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 16px;
    }
    button:hover {
        background-color: #45a049;
    }
    h2 {
        text-align: center;
        margin-bottom: 20px;
        color: #333;
    }
</style>
</head>
<body>
    <form action="family" method="post">
        <h2>Family Details</h2>
        
        
        <label for="name">Name</label>
        <input type="text" id="name" name="name" required>
        
        <label for="relation">Relation</label>
        <input type="text" id="relation" name="relation" required>
        
        <label for="dob">Date of Birth</label>
        <input type="date" id="dob" name="dob" required>
        
        <label for="age">Age</label>
        <input type="text" id="age" name="age" required>
        
        <label for="blood">Blood Group</label>
        <input type="text" id="blood" name="blood" required>
        
        <label for="gender">Gender</label>
        <input type="text" id="gender" name="gender" required>
        
        <label for="nationality">Nationality</label>
        <input type="text" id="nationality" name="nationality" required>
        
        <label for="profession">Profession</label>
        <input type="text" id="profession" name="profession" required>
        
        <button type="submit">Submit Details</button>
    </form>
</body>
</html>
