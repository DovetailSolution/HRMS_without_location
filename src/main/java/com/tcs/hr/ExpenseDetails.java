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

@WebServlet(name = "expensedetails", urlPatterns = { "/expensedetails" })
public class ExpenseDetails extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ExpenseDetails() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String empid = (String) session.getAttribute("empid");
        
        if (empid == null) {
            response.getWriter().println("Employee ID is missing.");
            return;
        }

        List<ExpenseRecord> records = new ArrayList<>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
             PreparedStatement ps = con.prepareStatement("SELECT * FROM expenses WHERE empId = ?")) {

            ps.setString(1, empid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ExpenseRecord record = new ExpenseRecord();
                record.setId(rs.getInt("id"));
                record.setEmpid(rs.getString("empid"));
                record.setUsername(rs.getString("username"));
                record.setTravel_route(rs.getString("travel_route"));
                record.setDate(rs.getDate("date"));
                record.setTimefrom(rs.getTime("time_from"));
                record.setTimeto(rs.getTime("time_to"));
                record.setPurpose(rs.getString("purpose"));
                record.setProject(rs.getString("project"));
                record.setExpenseincurred(rs.getDouble("expenses_incurred"));
                record.setAdvancetaken(rs.getDouble("advance_taken"));
                record.setMode(rs.getString("mode"));
                record.setTicketNo(rs.getString("ticket_no"));
                record.setTicketdate(rs.getDate("ticket_date"));
                record.setAttachment(rs.getString("attachment_path"));
                record.setStatus(rs.getString("status"));
                
                records.add(record);
            }

            rs.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        request.setAttribute("expense", records);
        request.getRequestDispatcher("expense.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
