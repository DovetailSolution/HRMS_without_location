package com.tcs.hr;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Period;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/salary")
public class Onboarding extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Onboarding() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String empId = request.getParameter("empId");
        String name = request.getParameter("name");
        String department = request.getParameter("dept");
        String joiningDate = request.getParameter("joining");
        String aadhar = request.getParameter("aadhar");
        String pan = request.getParameter("pan");
        String bankAccount = request.getParameter("bankaccount");
        String bankName = request.getParameter("bankname");
        String ifsc = request.getParameter("ifsc");
        BigDecimal ctc = new BigDecimal(request.getParameter("ctc"));
        BigDecimal basicSal = new BigDecimal(request.getParameter("basicsal"));
        BigDecimal da = new BigDecimal(request.getParameter("da"));
        BigDecimal hra = new BigDecimal(request.getParameter("hra"));
        BigDecimal ta = new BigDecimal(request.getParameter("ta"));
        BigDecimal ma = new BigDecimal(request.getParameter("ma"));
        
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            // Check if the employee already exists
            PreparedStatement checkStmt = con.prepareStatement("SELECT COUNT(*) FROM salary WHERE empId = ?");
            checkStmt.setString(1, empId);
            ResultSet rsCheck = checkStmt.executeQuery();

            LocalDate joinDate = LocalDate.parse(joiningDate);  // Convert String to LocalDate
            LocalDate currentDate = LocalDate.now();
            Period experience = Period.between(joinDate, currentDate);
            int yearsOfExperience = experience.getYears();
            int monthsOfExperience = experience.getMonths();
            int totalMonthsOfExperience = yearsOfExperience * 12 + monthsOfExperience;
            
            BigDecimal monthlySalary = basicSal.add(hra).add(da).add(ma).add(ta);
            
            
            System.out.println(totalMonthsOfExperience);
            System.out.println(currentDate);   

            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                // Employee already exists, redirect or show message
                request.setAttribute("errorMessage", "Employee details already exist.");
                request.getRequestDispatcher("salary.jsp").forward(request, response);
                return;
            } 
            else {
                // Insert salary information, including experience
            	// Insert salary information, including experience
            	// Insert salary information, including experience
            	PreparedStatement ps1 = con.prepareStatement(
            	    "INSERT INTO salary (empId, name, department, joining_date, aadhar_no, pan_no, bank_account_no, bank_name, ifsc, ctc, basic_salary, dearness_allowance, hra, travelling_allowance, medical_allowance, monthly_sal, experience) " +
            	    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            	);

            	// Set parameters
            	ps1.setString(1, empId);
            	ps1.setString(2, name);
            	ps1.setString(3, department);
            	ps1.setDate(4, Date.valueOf(joiningDate));
            	ps1.setString(5, aadhar);
            	ps1.setString(6, pan);
            	ps1.setString(7, bankAccount);
            	ps1.setString(8, bankName);
            	ps1.setString(9, ifsc);
            	ps1.setBigDecimal(10, ctc);
            	ps1.setBigDecimal(11, basicSal);
            	ps1.setBigDecimal(12, da);
            	ps1.setBigDecimal(13, hra);
            	ps1.setBigDecimal(14, ta);
            	ps1.setBigDecimal(15, ma);
            	ps1.setBigDecimal(16, monthlySalary);
            	ps1.setInt(17, totalMonthsOfExperience); // Insert total months of experience

            	// Execute the update
            	ps1.executeUpdate();

                // Redirect to HR dashboard after successful insertion
                response.sendRedirect("hr_dashboard.jsp");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while processing your request: " + e.getMessage());
            request.getRequestDispatcher("salary.jsp").forward(request, response);
        }
    }
}
