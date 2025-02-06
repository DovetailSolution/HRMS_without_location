package com.tcs.hr;

import java.io.PrintWriter;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

public class ReportGenerator {

    public static void generateAttendanceReport(HttpServletResponse response, Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement("SELECT u.empId, u.username, a.sign_in_time, a.location, a.sign_out_time, a.sign_out_location FROM attendance a JOIN users u ON a.user_id = u.id");
        ResultSet rs = ps.executeQuery();

        PrintWriter writer = response.getWriter();
        writer.println("Employee ID,Username,Sign In Time,Sign_in location,Sign Out Time,Sign_out location");

        while (rs.next()) {
            String empId = rs.getString("empId");
            String username = rs.getString("username");
            String signInTime = rs.getString("sign_in_time");
            String signInLocation=rs.getString("location");
            String signOutTime = rs.getString("sign_out_time");
            String signOutLocation=rs.getString("sign_out_location");

            writer.println(empId + "," + username + "," + signInTime + "," + signInLocation + "," + signOutTime + "," + signOutLocation);
        }

        writer.close();
    }

    public static void generateSalaryReport(HttpServletResponse response, Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM salary");
        ResultSet rs = ps.executeQuery();

        PrintWriter writer = response.getWriter();
        writer.println("Employee Id,Username,Department,Joining_date,Aadhar_no,Pan_no,Bank_account_no,Bank_name,CTC,Basic Salary,Dearness_allowance,HRA,Travelling_allowance,Medical_allowance,Leave_deduction,Time_deduction,Deduction_amount,Final_salary,Inserted_date");

        while (rs.next()) {
            String empId = rs.getString("empId");
            String name = rs.getString("name");
            String department = rs.getString("department");
            java.sql.Date joiningDate = rs.getDate("joining_date");
            String aadhar = rs.getString("aadhar_no");
            String pan = rs.getString("pan_no");
            String bankAccount = rs.getString("bank_account_no");
            String bankName = rs.getString("bank_name");
            BigDecimal ctc = rs.getBigDecimal("ctc");
            BigDecimal basicSal = rs.getBigDecimal("basic_salary");
            BigDecimal da = rs.getBigDecimal("dearness_allowance");
            BigDecimal hra = rs.getBigDecimal("hra");
            BigDecimal ta = rs.getBigDecimal("travelling_allowance");
            BigDecimal ma = rs.getBigDecimal("medical_allowance");
            BigDecimal ld=rs.getBigDecimal("leave_deduction");
            BigDecimal td=rs.getBigDecimal("time_deduction");
            BigDecimal deductionAmount = rs.getBigDecimal("deduction_amount");
            BigDecimal salary = rs.getBigDecimal("final_salary");

            writer.println(empId + "," + name + "," + department + "," + joiningDate + "," + aadhar + "," + pan + "," + bankAccount + "," + bankName + "," + ctc + "," + basicSal + "," + da + "," + hra + "," + ta + "," + ma + "," + ld + ","+ td + "," + deductionAmount + "," + salary);
        }

        writer.close();
    }

    public static void generatePivotedReport(HttpServletResponse response, Connection con) throws Exception {
        // Get the current month and year
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String firstDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, maxDay);
        String lastDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        // Query to get attendance and leave data
        PreparedStatement ps = con.prepareStatement(
            "SELECT u.empId, u.username, d.date AS attendance_date, " +
            "CASE " +
            "WHEN a.sign_in_time IS NOT NULL THEN 'Present' " +
            "WHEN lr.status = 'Approved' THEN ' ' " +
            "ELSE 'Absent' " +
            "END AS attendance_status " +
            "FROM users u " +
            "LEFT JOIN (SELECT DISTINCT DATE(sign_in_time) AS date FROM attendance WHERE sign_in_time BETWEEN ? AND ?) d ON TRUE " +
            "LEFT JOIN attendance a ON u.empId = a.empId AND DATE(a.sign_in_time) = d.date " +
            "LEFT JOIN leave_requests lr ON u.empId = lr.empId AND lr.status = 'Approved' AND d.date BETWEEN lr.start_date AND lr.end_date " +
            "ORDER BY u.empId, d.date;"
        );
        
        ps.setString(1, firstDate);
        ps.setString(2, lastDate);       
        ResultSet rs = ps.executeQuery();

        // Process the result set into a data structure for pivoting
        Map<String, Map<String, String>> attendanceData = new HashMap<>();
        Map<String, Integer> leaveCounts = new HashMap<>();
        Map<String, Integer> presentCounts = new HashMap<>();
        Map<String, Integer> absentCounts = new HashMap<>();

        while (rs.next()) {
            String empId = rs.getString("empId");
            String username = rs.getString("username");
            String attendanceDate = rs.getString("attendance_date");
            String attendanceStatus = rs.getString("attendance_status");

            if (!attendanceData.containsKey(empId)) {  
                attendanceData.put(empId, new HashMap<>());
                attendanceData.get(empId).put("username", username);
                presentCounts.put(empId, 0);
                absentCounts.put(empId, 0);
                leaveCounts.put(empId, 0); // Initialize leave count
            }
            attendanceData.get(empId).put(attendanceDate, attendanceStatus);

            // Increment the counts
            if (attendanceStatus.equals("Present")) {
                presentCounts.put(empId, presentCounts.get(empId) + 1);
            } else if (attendanceStatus.equals("Absent")) {
                absentCounts.put(empId, absentCounts.get(empId) + 1);
            } else if (attendanceStatus.equals("Leave")) {
                leaveCounts.put(empId, leaveCounts.get(empId) + 1);
            }
        }

        // Ensure every employee has an entry for each date
        for (String empId : attendanceData.keySet()) {
            Map<String, String> attendance = attendanceData.get(empId);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            for (int i = 1; i <= maxDay; i++) {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                attendance.putIfAbsent(date, "Absent");
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        // Write the CSV header
        PrintWriter writer = response.getWriter();
        writer.print("Employee ID,Username,Leave Count,Present Count,Absent Count");
        for (int i = 1; i <= maxDay; i++) {
            writer.print("," + i);
        }
        writer.println();

        // Write the CSV data
        for (Map.Entry<String, Map<String, String>> entry : attendanceData.entrySet()) {
            String empId = entry.getKey();
            Map<String, String> attendance = entry.getValue();
            writer.print(empId + "," + attendance.get("username") + "," + leaveCounts.getOrDefault(empId, 0) + "," +
                         presentCounts.getOrDefault(empId, 0) + "," + absentCounts.getOrDefault(empId, 0));
            cal.set(Calendar.DAY_OF_MONTH, 1);  // Reset the calendar to the first day of the month

            for (int i = 1; i <= maxDay; i++) {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                writer.print("," + attendance.getOrDefault(date, "Absent"));
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            writer.println();
        }
    }
    	
    	public static void generateExpenseReport(HttpServletResponse response, Connection con) throws Exception{
    		
    		PreparedStatement ps=con.prepareStatement("select * from expenses");
    		ResultSet rs=ps.executeQuery();
    		
    		PrintWriter writer=response.getWriter();
    		writer.println("Id,Emp Id,Username,Travel Route,Date,Time_from,Time_to,Purpose,Project,Expenses Incurred,Advance Taken,Mode,Ticket_no,Ticket_date");
    		
    		while(rs.next())
    		{
    			int id=rs.getInt("id");
    			String empid=rs.getString("empId");
    			String username=rs.getString("username");
    			String travelroute=rs.getString("travel_route");
    			Date date=rs.getDate("date");
    			Time time_from=rs.getTime("time_from");
    			Time time_to=rs.getTime("time_to");
    			String purpose=rs.getString("purpose");
    			String project=rs.getString("project");
    			BigDecimal expenseincurred=rs.getBigDecimal("expenses_incurred");
    			BigDecimal advanceTaken=rs.getBigDecimal("advance_taken");
    			String mode=rs.getString("mode");
    			String ticketNo=rs.getString("ticket_no");
    			Date ticketdate=rs.getDate("ticket_date");
    			
    			 writer.println(id + "," + empid + "," + username + "," + travelroute + "," + date + "," + time_from + "," + time_to + "," + purpose + "," + project + "," + expenseincurred + "," + advanceTaken + "," + mode + "," + ticketNo + "," + ticketdate);
    		}
    			writer.close();
    	}
    	
}
