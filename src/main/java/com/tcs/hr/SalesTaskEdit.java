package com.tcs.hr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "salestaskedit", urlPatterns = { "/salestaskedit" })
public class SalesTaskEdit extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SalesTaskEdit() {
        super();
    }

    // Display edit form with task details
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	String taskId = request.getParameter("taskId");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM client WHERE id = ?");
            ps.setString(1, taskId);
            ResultSet rs = ps.executeQuery();

            AttendanceRecord record = null;
            if (rs.next()) {
                
            	record=new AttendanceRecord();
            	record.setTaskid(rs.getInt("id"));
            	record.setCompanyname(rs.getString("company_name"));
            	record.setCompanyaddress(rs.getString("company_address"));
            	record.setSalestaskstatus(rs.getString("status"));
            	record.setMeetingOut(rs.getString("meeting_out"));
            }
            request.setAttribute("updatesale", record);
            request.getRequestDispatcher("editsaletask.jsp").forward(request, response);

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    // Handle update of status and meeting out
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    	String id = request.getParameter("taskId");
        String companyName=request.getParameter("companyName");
        String companyAddress=request.getParameter("companyAddress");
        String status = request.getParameter("status");
        String meetingout = request.getParameter("meetingout");

        String updatemeetingout=java.time.LocalTime.now().toString();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
            PreparedStatement ps = con.prepareStatement("UPDATE client SET status=?, meeting_out=? WHERE id=?");

            ps.setString(1, status);
            ps.setString(2, updatemeetingout);
            ps.setString(3, id);
            
            int rowsUpdated=ps.executeUpdate();

            if(rowsUpdated>0)
            {
            	request.setAttribute("message", "company updated successfully");
            }
            else {
            	request.setAttribute("message", "Failed to update");
            }
            
            response.sendRedirect("salestask.jsp");

            con.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
