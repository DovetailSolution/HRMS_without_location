package com.tcs.hr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "quotation", urlPatterns = { "/quotation" })
public class Quotation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Quotation() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	// QuotationServlet.java

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String customerName = request.getParameter("customerName");
	    String customerAddress = request.getParameter("customerAddress");
	    String customerPhone = request.getParameter("customerPhone");
	    String item1 = request.getParameter("item1");
	    String item2 = request.getParameter("item2");
	    int price1 = Integer.parseInt(request.getParameter("price1"));
	    int quantity1 = Integer.parseInt(request.getParameter("quantity1"));
	    int price2 = Integer.parseInt(request.getParameter("price2"));
	    int quantity2 = Integer.parseInt(request.getParameter("quantity2"));
	    
	    // Calculate the total amounts
	    int amount1 = price1 * quantity1;
	    int amount2 = price2 * quantity2;
	    
	    int subTotal = amount1 + amount2;
	    double tax1 = amount1 * 0.05;
	    double tax2 = amount2 * 0.05;
	    double amount3=amount1+tax1;
	    double amount4=amount2+tax2; 
	    
	    double totalTax=tax1+tax2;
	    double totalAmount = subTotal + totalTax;

	    // Save the quotation details to the database with 'pending' status
	   try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost/attendance_db","root","manager");
		
		PreparedStatement ps=con.prepareStatement("INSERT INTO quotations (customer_name, customer_address, customer_phone, item1, price1, quantity1, item2, price2, quantity2, sub_total, tax, total_amount, approval_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'pending')");
		ps.setString(1, customerName);
        ps.setString(2, customerAddress);
        ps.setString(3, customerPhone);
        ps.setString(4, item1);
        ps.setInt(5, price1);
        ps.setInt(6, quantity1);
        ps.setString(7, item2);
        ps.setInt(8, price2);
        ps.setInt(9, quantity2);
        ps.setInt(10, subTotal);
        ps.setDouble(11, totalTax);
        ps.setDouble(12, totalAmount);
        
        ps.executeUpdate();
		
	   	} catch (Exception e) {
	   		e.printStackTrace();
	   	}
	    	
	    // Forward to displayQuotation.jsp with the details
	    request.setAttribute("customerName", customerName);
	    request.setAttribute("customerAddress", customerAddress);
	    request.setAttribute("customerPhone", customerPhone);
	    request.setAttribute("item1", item1);
	    request.setAttribute("price1", price1);
	    request.setAttribute("quantity1", quantity1);
	    request.setAttribute("amount1", amount1);
	    request.setAttribute("item2", item2);
	    request.setAttribute("price2", price2);
	    request.setAttribute("quantity2", quantity2);
	    request.setAttribute("amount2", amount2);
	    request.setAttribute("subTotal", subTotal);
	    request.setAttribute("tax1", tax1);
	    request.setAttribute("tax2", tax2);
	    request.setAttribute("totalTax", totalTax);
	    request.setAttribute("totalAmount", totalAmount);
	    request.setAttribute("amount3", amount3);
	    request.setAttribute("amount4", amount4);	
	    
	    request.getRequestDispatcher("displayQuotation.jsp").forward(request, response);
	}

	}
