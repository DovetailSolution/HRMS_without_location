package com.tcs.hr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.jdbc.Driver;

@WebServlet(name = "updateleave", urlPatterns = { "/updateleave" })
public class updateLeave extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public updateLeave() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    	String id = request.getParameter("id");
        
        System.out.println(id);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            PreparedStatement ps1 = con.prepareStatement("SELECT * FROM leave_requests WHERE id = ?");
            ps1.setString(1, id);

            ResultSet rs1 = ps1.executeQuery();

            List<AttendanceRecord> leaves = new ArrayList<>();

            while (rs1.next()) {
                AttendanceRecord leave = new AttendanceRecord();
                leave.setId(rs1.getInt("id"));
                leave.setEmpId(rs1.getString("empId"));
                leave.setLeaveType(rs1.getString("leave_type"));
                leave.setStartDate(rs1.getTimestamp("start_date"));
                leave.setEndDate(rs1.getTimestamp("end_date"));
                leave.setRequester(rs1.getString("requester"));
                leave.setStatus(rs1.getString("status"));
                leave.setLeavePaymentType(rs1.getString("leave_payment_type"));

                leaves.add(leave);
            }

            request.setAttribute("leaves", leaves);
            request.getRequestDispatcher("updateLeave.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	String id=request.getParameter("id");
    	String empId = request.getParameter("empId");
        String leaveType = request.getParameter("leaveType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String requester = request.getParameter("requester");
        String status = request.getParameter("status");
        String leavePaymentType = request.getParameter("leavePaymentType");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            PreparedStatement ps = con.prepareStatement("UPDATE leave_requests SET leave_type=?, start_date=?, end_date=?, requester=?, status=?, leave_payment_type=? where id=?");
            ps.setString(1, leaveType);
            ps.setTimestamp(2, Timestamp.valueOf(startDate));
            ps.setTimestamp(3, Timestamp.valueOf(endDate));
            ps.setString(4, requester);
            ps.setString(5, status);
            ps.setString(6, leavePaymentType);
            ps.setString(7, id);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                request.setAttribute("message", "Leave updated successfully.");
            } else {
                request.setAttribute("message", "Failed to update leave.");
            }

            request.getRequestDispatcher("adminpanel.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
