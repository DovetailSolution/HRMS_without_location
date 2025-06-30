<!-- WebContent/quotationForm.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Quotation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
            margin: 0;
            padding: 20px;
        }
        h2 {
            text-align: center;
            color: #333;
        }
        .form-container {
            max-width: 700px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .form-container h3 {
            color: #0096C7;
            border-bottom: 2px solid #0096C7;
            padding-bottom: 5px;
        }
        label {
            font-weight: bold;
            margin-top: 10px;
            display: block;
        }
        input[type="text"], input[type="number"] {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
        }
        input[type="submit"] {
            width: 100%;
            padding: 10px;
            background-color: #0096C7;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #e74c3c;
        }
        .item-container {
            margin-top: 20px;
        }
    </style>
</head>
<body>

    <div class="form-container">
        <h2>Quotation Form</h2>
        <form action="quotation" method="post">
            <!-- Customer Details -->
            <div class="customer-details">
                <h3>Customer Details</h3>
                <label for="customerName">Customer Name:</label>
                <input type="text" id="customerName" name="customerName" required>

                <label for="customerAddress">Customer Address:</label>
                <input type="text" id="customerAddress" name="customerAddress" required>

                <label for="customerPhone">Phone:</label>
                <input type="text" id="customerPhone" name="customerPhone" required>
            </div>

            <!-- Item Details -->
            <div class="item-container">
                <h3>Item Details</h3>

                <label for="item1">Item 1:</label>
                <input type="text" id="item1" name="item1" required>

                <label for="price1">Price for Item 1:</label>
                <input type="number" id="price1" name="price1" required>

                <label for="quantity1">Quantity for Item 1:</label>
                <input type="number" id="quantity1" name="quantity1" required>

                <label for="item2">Item 2:</label>
                <input type="text" id="item2" name="item2" required>

                <label for="price2">Price for Item 2:</label>
                <input type="number" id="price2" name="price2" required>

                <label for="quantity2">Quantity for Item 2:</label>
                <input type="number" id="quantity2" name="quantity2" required>
            </div>

            <!-- Submit Button -->
            <input type="submit" value="Generate Quotation">
        </form>
    </div>

</body>
</html>
