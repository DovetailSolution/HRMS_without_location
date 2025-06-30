<!-- WebContent/displayQuotation.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Generated Quotation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 20px;
            background-color: #f9f9f9;
        }
        h2, h3 {
            color: #0096C7;
        }
        .header, .footer {
            text-align: center;
            margin-bottom: 20px;
        }
        .header h2 {
            margin-bottom: 5px;
        }
        .header p {
            margin: 0;
        }
        .quotation-details, .items-table, .summary, .bank-details {
            margin-bottom: 20px;
            width: 100%;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        table, th, td {
            border: 1px solid #ccc;
        }
        th, td {
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f1f1f1;
        }
        .total {
            font-weight: bold;
        }
        .footer p {
            font-size: 14px;
        }
        .right {
            text-align: right;
        }
        .items-header {
            background-color: #d63031;
            color: #fff;
        }
        .table-footer td {
            background-color: #f1f1f1;
            font-weight: bold;
        }
    </style>
</head>
<body>

    <!-- Header Section -->
    <div class="header">
        <h2>Dovetail Solutions</h2>
        <p>Office No-312, D&E, Block CCC, VIP Rd, Zirakpur, Punjab 140603<br>
        Phone: +91 9418083555<br>
        GSTIN: 02AAMFD1861G1ZQ<br>
        PAN Number: AAMFD1861G</p>
    </div>

    <!-- Quotation Details -->
    <div class="quotation-details">
        <table>
            <tr>
                <td><strong>Quotation To:</strong></td>
                <td><strong>Ship To:</strong></td>
            </tr>
            <tr>
                <td>
                    <p><%= request.getAttribute("customerName") %><br>
                    <%= request.getAttribute("customerAddress") %><br>
                    Phone: <%= request.getAttribute("customerPhone") %></p>
                </td>
                <td>
                    <p><%= request.getAttribute("customerName") %><br>
                    (Same as Quotation To)</p>
                </td>
            </tr>
        </table>
    </div>

    <!-- Items Table -->
    <h3>Items</h3>
    <table class="items-table">
        <thead>
            <tr class="items-header">
                <th>Items</th>
                <th>Quantity</th>
                <th>Price per Unit</th>
                <th>Tax per Unit</th>
                <th>Amount</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><%= request.getAttribute("item1") %></td>
                <td><%= request.getAttribute("quantity1") %></td>
                <td>Rs. <%= request.getAttribute("price1") %></td>
                <td>Rs. <%= request.getAttribute("tax1") %> (5%)</td>
                <td>Rs. <%= request.getAttribute("amount3") %></td>
            </tr>
            <tr>
                <td><%= request.getAttribute("item2") %></td>
                <td><%= request.getAttribute("quantity2") %></td>
                <td>Rs. <%= request.getAttribute("price2") %></td>
                <td>Rs. <%= request.getAttribute("tax2") %> (5%)</td>
                <td>Rs. <%= request.getAttribute("amount4") %></td>
            </tr>
            <!-- Add more items dynamically if needed -->
        </tbody>
        <tfoot>
            <tr class="table-footer">
                <td colspan="4" class="right">Sub Total:</td>
                <td>Rs. <%= request.getAttribute("subTotal") %></td>
            </tr>
            <tr class="table-footer">
                <td colspan="4" class="right">Total Tax:</td>
                <td>Rs. <%= request.getAttribute("totalTax") %></td>
            </tr>
            <tr class="table-footer">
                <td colspan="4" class="right">Discount:</td>
                <td>- Rs. <%= request.getAttribute("discount") %></td>
            </tr>
            <tr class="table-footer total">
                <td colspan="4" class="right">Total Amount:</td>
                <td>Rs. <%= request.getAttribute("totalAmount") %></td>
            </tr>
        </tfoot>
    </table>

    <!-- Bank Details -->
    <h3>Bank Details</h3>
    <p>
        Account Holder: Dovetail Solutions<br>
        Account Number: 5243736409962011<br>
        Bank: RBL<br>
        Branch: Zirakpur<br>
        IFSC Code: RATN0000233<br>
        UPI ID: 123@rbl
    </p>

    <!-- Footer Section -->
    <div class="footer">
        <p><strong>Notes:</strong><br>
        1. No return deal</p>
        <p><strong>Terms & Conditions:</strong><br>
        1. Customer will pay the GST<br>
        2. Customer will pay the delivery charges<br>
        3. Pay due amount within 15 days</p>

        <p><strong>Thank You For Your Business!</strong></p>
    </div>

</body>
</html>
