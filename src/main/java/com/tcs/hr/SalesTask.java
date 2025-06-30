package com.tcs.hr;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tcs.hr.AttendanceRecord;

@WebServlet(name = "salestask", urlPatterns = { "/salestask" })
public class SalesTask extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SalesTask() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	
        	HttpSession session=request.getSession();
        	String empid=(String) session.getAttribute("empid");
            // Database connection
        	
        	System.out.println(empid);
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            // Query to fetch all existing records
            PreparedStatement ps = con.prepareStatement("SELECT * FROM client where empid=?");
            ps.setString(1, empid);
            ResultSet rs = ps.executeQuery();

            // Create a list to hold the records
            List<AttendanceRecord> records = new ArrayList<>();
            while (rs.next()) {
                AttendanceRecord record = new AttendanceRecord();
                record.setTaskid(rs.getInt("id"));
                record.setSaledate(rs.getDate("date"));
                record.setCompanyname(rs.getString("company_name"));
                record.setCompanyaddress(rs.getString("company_address"));
                record.setClient(rs.getString("client_name"));
                record.setClientdesignation(rs.getString("designation"));
                record.setClientmobile(rs.getString("mobile"));
                record.setClientemail(rs.getString("email"));
                record.setWork(rs.getString("work"));
                record.setClientamount(rs.getInt("Amount_received"));
                record.setMeetingIn(rs.getString("meeting_in"));
                record.setMeetingOut(rs.getString("meeting_out"));
                record.setSalestaskstatus(rs.getString("status"));

                records.add(record);
            }

            // Set the records in the request attribute to pass to JSP
            request.setAttribute("saletask", records);

            // Forward request to JSP page
            request.getRequestDispatcher("salestask.jsp").forward(request, response);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            HttpSession session = request.getSession();
            if ("meetingin".equals(action)) {
                // Meeting In action logic
                String meetingInTime = java.time.LocalTime.now().toString();
                session.setAttribute("meetingIn", meetingInTime);
            } else {
                // Handle form submission for new task
                String companyName = request.getParameter("companyName");
                String companyAddress = request.getParameter("companyAddress");
                String clientName = request.getParameter("clientName");
                String designation = request.getParameter("designation");
                String mobileNo = request.getParameter("mobileNo");
                String email = request.getParameter("email");
                String work = request.getParameter("work");
                String amount = request.getParameter("amount");
                String meetingin = (String) session.getAttribute("meetingIn");
                String date=java.time.LocalDate.now().toString();
                String empid=(String) session.getAttribute("empid");
                String username=(String) session.getAttribute("user");
                // Insert the new record into the database
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO client(username, empId, date, company_name, company_address, client_name, designation, mobile, email, work, Amount_received, meeting_in) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                ps.setString(1, username);
                ps.setString(2, empid);
                ps.setString(3, date);
                ps.setString(4, companyName);
                ps.setString(5, companyAddress);
                ps.setString(6, clientName);
                ps.setString(7, designation);
                ps.setString(8, mobileNo);
                ps.setString(9, email);
                ps.setString(10, work);
                ps.setString(11, amount);
                ps.setString(12, meetingin);
                ps.executeUpdate();
                con.close();
            }

            // Redirect to doGet to refresh the list
            doGet(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    }

