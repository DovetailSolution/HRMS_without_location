package com.tcs.hr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "salestask", urlPatterns = {"/salestask"})
public class SalesTask extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        try {
            if ("viewDetails".equals(action)) {
                String empid = (String) session.getAttribute("empid");
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

                // Fetch client details
                PreparedStatement ps = con.prepareStatement("SELECT * FROM client WHERE empid = ?");
                ps.setString(1, empid);
                ResultSet rs = ps.executeQuery();

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
                    record.setRemarks(rs.getString("remarks"));
                    record.setSalestaskstatus(rs.getString("status"));

                    records.add(record);
                }

                // Pass records to JSP
                request.setAttribute("saletask", records);
                con.close();
            }

            request.getRequestDispatcher("salestask.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	String companyName = request.getParameter("companyName");
        String companyAddress = request.getParameter("companyAddress");
        String clientName = request.getParameter("clientName"); 
    	String action = request.getParameter("action");
        HttpSession session = request.getSession();

        try {
            if ("meetingin".equals(action)) 
            {
                String meetingInTime = LocalTime.now().toString();
                
                session.setAttribute("meetingIn", meetingInTime);
                session.setAttribute("companyname", companyName);
                session.setAttribute("companyaddress", companyAddress);
                session.setAttribute("clientname", clientName);
                
                session.setAttribute("meetingStarted", true); // Enable form visibility
            } 
            else if ("meetingout".equals(action)) 
            {
                String meetingOutTime = LocalTime.now().toString();
                session.setAttribute("meetingOut", meetingOutTime);
                session.removeAttribute("meetingStarted"); // Reset after form submission

                // Save form data
                String company = (String) session.getAttribute("companyname");
                String address = (String) session.getAttribute("companyaddress");
                String client = (String) session.getAttribute("clientname");
                String designation = request.getParameter("designation");
                String mobileNo = request.getParameter("mobileNo");
                String email = request.getParameter("email");
                String work = request.getParameter("work");
                String amount = request.getParameter("amount");
                String remarks=request.getParameter("remarks");
                String meetingIn = (String) session.getAttribute("meetingIn");
                String date = java.time.LocalDate.now().toString();
                String empid = (String) session.getAttribute("empid");
                String username = (String) session.getAttribute("user");

                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
                
                PreparedStatement ps1=con.prepareStatement("update client set status=?");
                ps1.setString(1, "Completed");
                
                ps1.executeUpdate();
                
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO client(username, empId, date, company_name, company_address, client_name, designation, mobile, email, work, Amount_received, remarks, meeting_in, meeting_out) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                ps.setString(1, username);
                ps.setString(2, empid);
                ps.setString(3, date);
                ps.setString(4, company);
                ps.setString(5, address);
                ps.setString(6, client);
                ps.setString(7, designation);
                ps.setString(8, mobileNo);
                ps.setString(9, email);
                ps.setString(10, work);
                ps.setString(11, amount);
                ps.setString(12, remarks);
                ps.setString(13, meetingIn);
                ps.setString(14, meetingOutTime);
                ps.executeUpdate();
                con.close();
            }

            // Redirect to doGet to handle next action
            doGet(request, response);
           
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
