package com.tcs.hr;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/salarymodule")
public class salarymodule extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public salarymodule() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
            
            // Fetch all employees
            PreparedStatement fetchEmployeesStmt = con.prepareStatement("SELECT * FROM salary");
            ResultSet rsEmployees = fetchEmployeesStmt.executeQuery();

            while (rsEmployees.next()) {
                String empId = rsEmployees.getString("empId");
                BigDecimal basicSal = rsEmployees.getBigDecimal("basic_salary");

       //------------------------------------------------------------------  
                // Fetch attendance information for the employee
                PreparedStatement ps = con.prepareStatement(
                    "SELECT sign_in_time, sign_out_time FROM attendance WHERE empId = ?"
                );
                ps.setString(1, empId);
                ResultSet rsAttendance = ps.executeQuery();

                BigDecimal deductionAmount = BigDecimal.ZERO;
                BigDecimal timeDeduction= BigDecimal.ZERO;
                BigDecimal leaveDeduction=BigDecimal.ZERO;
                
                while (rsAttendance.next()) {
                    java.sql.Timestamp signInTime = rsAttendance.getTimestamp("sign_in_time");
                    java.sql.Timestamp signOutTime = rsAttendance.getTimestamp("sign_out_time");

                    if (signInTime != null && signOutTime != null) {
                        long milliseconds = signOutTime.getTime() - signInTime.getTime();
                        long hoursWorked = milliseconds / (1000 * 60 * 60);

                        BigDecimal perDaySalary = basicSal.divide(new BigDecimal(30), BigDecimal.ROUND_HALF_UP);

                        if (hoursWorked < 4) 
                        {
                            // Deduct full day's salary
                        	timeDeduction = timeDeduction.add(perDaySalary);
                        } 
                        else if (hoursWorked >= 4 && hoursWorked < 8) {
                            // Deduct half day's salary  
                        	timeDeduction = timeDeduction.add(perDaySalary.divide(new BigDecimal(2), BigDecimal.ROUND_HALF_UP));
                        }
                        System.out.println(timeDeduction);
                    }
                }
       //--------------------------------------------------------------------- 
                
                // Fetch leave information for the employee
                PreparedStatement leavePs = con.prepareStatement(
                    "SELECT leave_payment_type, COUNT(*) AS days_taken " +
                    "FROM leave_requests WHERE empId = ? GROUP BY leave_payment_type"
                );
                
                leavePs.setString(1, empId);
                ResultSet rsLeave = leavePs.executeQuery();

                while (rsLeave.next()) {
                    String leavePaymentType = rsLeave.getString("leave_payment_type");
                    int daysTaken = rsLeave.getInt("days_taken");

                    System.out.println(daysTaken);
                    
                    if ("Unpaid".equalsIgnoreCase(leavePaymentType)) {
                        BigDecimal perDaySalary = basicSal.divide(new BigDecimal(30), BigDecimal.ROUND_HALF_UP);
                        leaveDeduction = leaveDeduction.add(perDaySalary.multiply(new BigDecimal(daysTaken)));
                    }
                    System.out.println(leaveDeduction);
                }
        //----------------------------------------------------------------------
                deductionAmount=leaveDeduction.add(timeDeduction);
                // Calculate final salary
                BigDecimal finalSalary = basicSal.subtract(deductionAmount);
       //--------------------------------------------------------------------------
                // Update salary information
                PreparedStatement ps1 = con.prepareStatement(
                    "UPDATE salary SET leave_deduction=?, time_deduction=?, deduction_amount=?, final_salary=? WHERE empId=?"
                );
                ps1.setBigDecimal(1, leaveDeduction);
                ps1.setBigDecimal(2, timeDeduction);
                ps1.setBigDecimal(3, deductionAmount);
                ps1.setBigDecimal(4, finalSalary);
                ps1.setString(5, empId);

                ps1.executeUpdate();
            }

            response.sendRedirect("hr_dashboard.jsp");
        } 
        catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while processing your request: " + e.getMessage());
            request.getRequestDispatcher("salary.jsp").forward(request, response);
        }
    }
}



