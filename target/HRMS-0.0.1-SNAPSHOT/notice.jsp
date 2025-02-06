<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Submit Notice</title>
<style>
    /* General page styling */
    body {
        font-family: Arial, sans-serif;
        background-color: #f8f9fa;
        margin: 0;
        padding: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
    }

    /* Form container */
    .form-container {
        background-color: #ffffff;
        border: 1px solid #ddd;
        border-radius: 8px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        padding: 20px 40px;
        width: 400px;
        text-align: center;
    }

    /* Form title */
    h1 {
        color: #333333;
        margin-bottom: 20px;
    }

    /* Labels and Inputs */
    label {
        display: block;
        text-align: left;
        font-weight: bold;
        margin-bottom: 5px;
        color: #555;
    }

    input[type="date"], input[type="text"] {
        width: 100%;
        padding: 10px;
        margin-bottom: 20px;
        border: 1px solid #ccc;
        border-radius: 4px;
        font-size: 16px;
        box-sizing: border-box;
    }

    input:focus {
        border-color: #007bff;
        outline: none;
        box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
    }

    /* Submit Button */
    button {
        background-color: #007bff;
        color: #ffffff;
        border: none;
        padding: 10px 20px;
        font-size: 16px;
        font-weight: bold;
        border-radius: 4px;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    button:hover {
        background-color: #0056b3;
    }
</style>
</head>
<body>
    <div class="form-container">
        <h1>Submit Notice</h1>
        <form action="notice" method="post">
            <label for="date">Date:</label>
            <input type="date" name="date" id="date" required>
            
            <label for="message">Message:</label>
            <input type="text" name="message" id="message" placeholder="Enter your notice message" required>
            
            <button type="submit">Submit</button>
        </form>
    </div>
</body>
</html>
