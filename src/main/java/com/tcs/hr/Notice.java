package com.tcs.hr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "notice", urlPatterns = { "/notice" })
public class Notice extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Notice() {
        super();
    }

    // GET method to fetch notices
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isApiRequest = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        JSONObject jsonResponse = new JSONObject();
        JSONArray noticeArray = new JSONArray();
        
        List<Map<String, Object>> noticesList = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
            ps = con.prepareStatement("SELECT * FROM notice");
            rs = ps.executeQuery();

            while (rs.next()) {
                JSONObject record = new JSONObject();
                Date date = rs.getDate("date");
                String message = rs.getString("message");

                record.put("Date", date);
                record.put("Message", message);
                noticeArray.put(record);

                // Store in list for JSP
                Map<String, Object> notice = new HashMap<>();
                notice.put("Date", date);
                notice.put("Message", message);
                noticesList.add(notice);
            }

            if (isApiRequest) {
                jsonResponse.put("status", "success");
                jsonResponse.put("Notice", noticeArray);

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonResponse.toString());
                response.getWriter().flush();
            } 
            else 
            {
                request.setAttribute("noticesList", noticesList);
                request.getRequestDispatcher("urgentnotice.jsp").forward(request, response);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving notices.");
        } 
        finally 
        {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } 
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    // POST method to insert a new notice
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String date = request.getParameter("date");
        String message = request.getParameter("message");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
                 PreparedStatement ps = con.prepareStatement("INSERT INTO notice (date, message) VALUES (?, ?)")) {

                ps.setString(1, date);
                ps.setString(2, message);

                int rowsUpdated = ps.executeUpdate();

                if (rowsUpdated > 0) {
                    // Redirect to HR dashboard after successful insertion
                    response.sendRedirect("hr_dashboard.jsp");
                } else {
                    response.getWriter().println("Failed to send notice.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving notice.");
        }
    }
}
