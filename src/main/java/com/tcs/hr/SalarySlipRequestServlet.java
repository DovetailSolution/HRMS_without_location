package com.tcs.hr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "salarySlipRequest", urlPatterns = { "/salarySlipRequest" })
public class SalarySlipRequestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String empId = (String) session.getAttribute("empid");
        String username=(String) session.getAttribute("user");
        String slipMonth=request.getParameter("month");
        String action = request.getParameter("action");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            if ("requestSalarySlip".equals(action)) {
                // Insert a new salary slip request for the employee
                PreparedStatement ps = con.prepareStatement("INSERT INTO salary_slip_request (empId, username, request_date, slip_month, approval_status) VALUES (?, ?, NOW(), ?, 'Pending')");
                ps.setString(1, empId);
                ps.setString(2, username);
                ps.setString(3, slipMonth);
                ps.executeUpdate();
                ps.close();

                // Notify user and redirect to their dashboard
                session.setAttribute("statusMessage", "Salary slip request sent to HR.");
                response.sendRedirect("dashboard.jsp");

            } 
            else if ("slipstatus".equals(action))
            {
                // Retrieve the status of salary slip requests for the employee
                PreparedStatement ps = con.prepareStatement("SELECT * FROM salary_slip_request WHERE empId = ?");
                
                ps.setString(1, empId);
                ResultSet rs = ps.executeQuery();

                List<AttendanceRecord> requests = new ArrayList<>();
                while (rs.next()) {
                    AttendanceRecord requestRecord = new AttendanceRecord();
                    requestRecord.setId(rs.getInt("id"));
                    requestRecord.setEmpId(rs.getString("empId"));
                    requestRecord.setUsername(rs.getString("username"));
                    requestRecord.setRequestDate(rs.getTimestamp("request_date"));
                    requestRecord.setSlipstatus(rs.getString("approval_status"));
                    requestRecord.setActionedDate(rs.getTimestamp("actioned_date"));
                    requestRecord.setRemarks(rs.getString("remarks"));
                    
                    System.out.println(rs.getInt("id"));
                    System.out.println(rs.getString("empId"));
                    System.out.println(rs.getString("username"));
                   
                    requests.add(requestRecord);
                }

                request.setAttribute("slipRequestStatus", requests);
                request.getRequestDispatcher("salaryslipstatus.jsp").forward(request, response);
                
                rs.close();
                ps.close();
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
