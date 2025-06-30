							package com.tcs.hr;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

@WebServlet(name = "hr", urlPatterns = { "/hr" })
public class HRServlet extends HttpServlet {
	
	private static final String UPLOAD_DIRECTORY = "uploads/";
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"hr".equals(role)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String yearMonthParam = request.getParameter("yearMonth");
        List<AttendanceRecord> attendanceRecords = new ArrayList<>();
        List<AttendanceRecord> leaveRequests = new ArrayList<>();
        List<AttendanceRecord> salarySlipRequests = new ArrayList<>();
        List<ExpenseRecord> expenseRecord=new ArrayList<ExpenseRecord>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
     //------------------------------------------------------------------------------------------
            // Fetch attendance records
            if (yearMonthParam != null) {
                YearMonth yearMonth = YearMonth.parse(yearMonthParam);
                LocalDate firstDayOfMonth = yearMonth.atDay(1);
                LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
                Timestamp startTimestamp = Timestamp.valueOf(firstDayOfMonth.atStartOfDay());
                Timestamp endTimestamp = Timestamp.valueOf(lastDayOfMonth.atTime(LocalTime.MAX));

                PreparedStatement ps = con.prepareStatement(
                    "SELECT u.empId, u.username, a.sign_in_time, a.sign_out_time, a.location, a.sign_out_location, a.latitude, a.longitude " +
                    "FROM attendance a JOIN users u ON a.user_id = u.id " +
                    "WHERE a.sign_in_time BETWEEN ? AND ?"
                );
                ps.setTimestamp(1, startTimestamp);
                ps.setTimestamp(2, endTimestamp);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    AttendanceRecord record = new AttendanceRecord();
                    record.setEmpId(rs.getString("empId"));
                    record.setUsername(rs.getString("username"));
                    record.setSignInTime(rs.getTimestamp("sign_in_time"));
                    record.setSignOutTime(rs.getTimestamp("sign_out_time"));
                    record.setLocation(rs.getString("location"));
                    record.setSignOutLocation(rs.getString("sign_out_location"));
                    record.setLatitude(rs.getString("latitude"));
                    record.setLongitude(rs.getString("longitude"));
                    attendanceRecords.add(record);
                }
                rs.close();
                ps.close();
            }
    //-----------------------------------------------------------------------------------------------
            // Fetch leave requests
            PreparedStatement psLeave = con.prepareStatement(
                "SELECT lr.id, u.empId, u.username, lr.leave_type, lr.start_date, lr.end_date, lr.requester, lr.status, lr.leave_payment_type " +
                "FROM leave_requests lr JOIN users u ON lr.empId = u.empId " +
                "WHERE lr.status = 'Pending'"
            );
            ResultSet rsLeave = psLeave.executeQuery();

            while (rsLeave.next()) {
                AttendanceRecord leaveRequest = new AttendanceRecord();
                leaveRequest.setId(rsLeave.getInt("id"));
                leaveRequest.setEmpId(rsLeave.getString("empId"));
                leaveRequest.setUsername(rsLeave.getString("username"));
                leaveRequest.setLeaveType(rsLeave.getString("leave_type"));
                leaveRequest.setStartDate(rsLeave.getTimestamp("start_date"));
                leaveRequest.setEndDate(rsLeave.getTimestamp("end_date"));
                leaveRequest.setRequester(rsLeave.getString("requester"));
                leaveRequest.setStatus(rsLeave.getString("status"));
                leaveRequests.add(leaveRequest);
            }
            rsLeave.close();
            psLeave.close();
     //-----------------------------------------------------------------------------------------------
            // Fetch salary slip requests
            PreparedStatement psSalary = con.prepareStatement(
                "SELECT * FROM salary_slip_request WHERE approval_status = 'Pending'"
            );
            ResultSet rsSalary = psSalary.executeQuery();

            while (rsSalary.next()) {
                AttendanceRecord requestRecord = new AttendanceRecord();
                requestRecord.setEmpId(rsSalary.getString("empId"));
                requestRecord.setUsername(rsSalary.getString("username"));
                requestRecord.setRequestDate(rsSalary.getTimestamp("request_date"));
                requestRecord.setSlipMonth(rsSalary.getString("slip_month"));
                requestRecord.setSlipstatus(rsSalary.getString("approval_status"));
                requestRecord.setActionedDate(rsSalary.getTimestamp("actioned_date"));
                requestRecord.setRemarks(rsSalary.getString("remarks"));
                salarySlipRequests.add(requestRecord); // Add to the list
            }
            rsSalary.close();
            psSalary.close();
            
   //----------------------------------------------------------------------------------------------
            //Fetch expense details
            PreparedStatement psexpense=con.prepareStatement("select * from expenses where status='pending'");
            ResultSet rsexpense=psexpense.executeQuery();
            
            while(rsexpense.next())
            {
            	ExpenseRecord record=new ExpenseRecord();
            	record.setId(rsexpense.getInt("id"));
            	record.setEmpid(rsexpense.getString("empid"));
                record.setUsername(rsexpense.getString("username"));
                record.setTravel_route(rsexpense.getString("travel_route"));
                record.setDate(rsexpense.getDate("date"));
                record.setTimefrom(rsexpense.getTime("time_from"));
                record.setTimeto(rsexpense.getTime("time_to"));
                record.setPurpose(rsexpense.getString("purpose"));
                record.setProject(rsexpense.getString("project"));
                record.setExpenseincurred(rsexpense.getDouble("expenses_incurred"));
                record.setAdvancetaken(rsexpense.getDouble("advance_taken"));
                record.setMode(rsexpense.getString("mode"));
                record.setTicketNo(rsexpense.getString("ticket_no"));
                record.setTicketdate(rsexpense.getDate("ticket_date"));
                String attachmentPath = rsexpense.getString("attachment_path");
                
                if (attachmentPath != null) {
                    // Extract the file name from the attachment path
                    String fileName = new File(attachmentPath).getName();

                    // Encode only spaces and special characters
                    String encodedFileName = fileName.replace(" ","%20");
                    
                    // Construct the server file URL
                    String serverFileUrl = response.encodeURL(request.getContextPath() + "/" + UPLOAD_DIRECTORY + encodedFileName);

                    record.setAttachment(serverFileUrl);
                }
                record.setStatus(rsexpense.getString("status"));
            	
                expenseRecord.add(record);
            }

            request.setAttribute("ExpenseDetails", expenseRecord);
            request.setAttribute("attendanceRecords", attendanceRecords);
            request.setAttribute("leaveRequests", leaveRequests);
            request.setAttribute("salarySlipRequests", salarySlipRequests); // Set salary slip requests
            request.getRequestDispatcher("hr_dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while processing your request: " + e.getMessage());
            request.getRequestDispatcher("hr_dashboard.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	String leaveId = request.getParameter("leaveId");  // For leave requests
        String leavePaymentType = request.getParameter("leavePaymentType");
        String decision = request.getParameter("decision");

        String empId = request.getParameter("empId");      // For salary slip requests
        String salaryAction = request.getParameter("action");  // Action for salary slip (approve or reject)
        String expenseAction=request.getParameter("action");
        String expenseid=request.getParameter("expenseid");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

    //----------------------------------------------------------------------------------------------
            // Handle leave request update
            if (leaveId != null && leavePaymentType != null && decision != null) 
            {
                PreparedStatement ps = con.prepareStatement(
                    "UPDATE leave_requests SET leave_payment_type = ?, status = ? WHERE id = ?"
                );
                ps.setString(1, leavePaymentType);
                ps.setString(2, decision);
                ps.setInt(3, Integer.parseInt(leaveId));
                ps.executeUpdate();
                ps.close();
            }
   //-------------------------------------------------------------------------------------------------
            // Handle salary slip request update
            if (empId != null && salaryAction != null) {
                PreparedStatement psSalary = con.prepareStatement(
                    "UPDATE salary_slip_request SET approval_status = ? WHERE empId = ?"
                );
                psSalary.setString(1, salaryAction.equals("approve") ? "Approved" : "Rejected");
                psSalary.setString(2, empId);
                psSalary.executeUpdate();
                psSalary.close();
            }
   //---------------------------------------------------------------------------------------------------
            //Handle expense details update
            if("Send_To_Admin".equals(expenseAction))
            {
            	PreparedStatement psexpense=con.prepareStatement("update expenses set status=? where id=?");
            	
            	psexpense.setString(1, expenseAction);
            	psexpense.setString(2, expenseid);
            	psexpense.executeUpdate();
            	
            	psexpense.close();
            }
            else if("Rejected_by_HR".equals(expenseAction))
            {
            	PreparedStatement psexpense=con.prepareStatement("update expenses set status=? where id=?");
            	
            	psexpense.setString(1, expenseAction);
            	psexpense.setString(2, expenseid);
            	psexpense.executeUpdate();
            	
            	psexpense.close();
            }
            else if("Approved_by_HR".equals(expenseAction))
            {
            	PreparedStatement psexpense=con.prepareStatement("update expenses set status=? where id=?");
            	
            	psexpense.setString(1, expenseAction);
            	psexpense.setString(2, expenseid);
            	psexpense.executeUpdate();
            	
            	psexpense.close();
            }
            	
            // Redirect to refresh the HR dashboard after processing
            response.sendRedirect("hr");
        } 
        catch (Exception e) 				
        {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while processing your request: " + e.getMessage());
            request.getRequestDispatcher("hr_dashboard.jsp").forward(request, response);
        }
    }
}
