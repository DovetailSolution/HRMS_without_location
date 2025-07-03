package com.tcs.hr;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import io.jsonwebtoken.*;
import org.json.*;

// POJO you already use in JSPs
import com.tcs.hr.AttendanceRecord;

/**
 * /leave servlet
 * POST ?action=submit  → employee submits a leave request
 * POST ?action=status  → employee views their leave requests
 */
@WebServlet(name = "leave", urlPatterns = "/leave")
public class LeaveServlet extends HttpServlet {

    /* =========================  POST DISPATCHER  ========================= */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        /* --- 1. JWT authentication from header or session --- */
        Claims claims = JwtUtil.extractToken(req)
                               .map(t -> JwtUtil.verify(t).getBody())
                               .orElse(null);
        if (claims == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Missing or invalid token");
            return;
        }

        String empId   = claims.get("empid",  String.class);
        Integer userId = claims.get("userid", Integer.class);
        String user    = claims.getSubject();

        String action  = req.getParameter("action");
        boolean isApi  = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
        JSONObject json = new JSONObject();

        switch (action) {
            case "submit":
                submitLeave(req, res, isApi, json, empId, userId, user);
                break;
            case "status":
                statusLeave(req, res, isApi, json, empId);
                break;
            default:
                res.sendError(HttpServletResponse.SC_BAD_REQUEST,"Unknown action");
        }
    }

    /* =========================  ACTION HANDLERS  ========================= */

    /** ---------- submit new leave request ---------- */
    private void submitLeave(HttpServletRequest req, HttpServletResponse res,
                             boolean isApi, JSONObject json,
                             String empId, Integer userId, String requester) throws IOException {

        String leaveType    = req.getParameter("leaveType");
        String startStr     = req.getParameter("startDate");
        String endStr       = req.getParameter("endDate");
        String remarks      = req.getParameter("leaveRemarks");

        if (userId == null || leaveType == null || startStr == null || endStr == null) {
            errorJson(isApi, res, json, "Missing parameters", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        LocalDate start = LocalDate.parse(startStr);
        LocalDate end   = LocalDate.parse(endStr);
        int daysTaken   = (int) ChronoUnit.DAYS.between(start, end) + 1;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO leave_requests " +
                "(user_id, empId, leave_type, start_date, end_date, leave_remarks, requester, status, days_taken) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 'Pending', ?)")) {

            ps.setInt   (1, userId);
            ps.setString(2, empId);
            ps.setString(3, leaveType);
            ps.setDate  (4, java.sql.Date.valueOf(start));
            ps.setDate  (5, java.sql.Date.valueOf(end));
            ps.setString(6, remarks);
            ps.setString(7, requester);
            ps.setInt   (8, daysTaken);

            if (ps.executeUpdate() > 0) {
                if (isApi) {
                    json.put("status","success")
                        .put("message","Leave request submitted")
                        .put("leaveType", leaveType)
                        .put("startDate", startStr)
                        .put("endDate",   endStr)
                        .put("daysTaken", daysTaken);
                    sendJson(res, HttpServletResponse.SC_OK, json);
                } else {
                    res.sendRedirect("dashboard.jsp?message=Leave+request+submitted");
                }
            } else {
                errorJson(isApi,res,json,"Insert failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            errorJson(isApi,res,json,"DB error");
        }
    }

    /** ---------- view leave status ---------- */
    private void statusLeave(HttpServletRequest req, HttpServletResponse res,
                             boolean isApi, JSONObject json,
                             String empId) throws IOException, ServletException {

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                "SELECT user_id, leave_type, start_date, end_date, requester, " +
                "status, leave_payment_type, days_taken " +
                "FROM leave_requests WHERE empId = ?")) {

            ps.setString(1, empId);

            try (ResultSet rs = ps.executeQuery()) {

                JSONArray apiArray            = new JSONArray();       // for API
                List<AttendanceRecord> jspList = new ArrayList<>();    // for JSP

                while (rs.next()) {
                    /* Build JSON object */
                    JSONObject row = new JSONObject()
                        .put("userId",           rs.getInt("user_id"))
                        .put("leaveType",        rs.getString("leave_type"))
                        .put("startDate",        rs.getDate("start_date").toString())
                        .put("endDate",          rs.getDate("end_date").toString())
                        .put("requester",        rs.getString("requester"))
                        .put("status",           rs.getString("status"))
                        .put("leavePaymentType", rs.getString("leave_payment_type"))
                        .put("daysTaken",        rs.getInt("days_taken"));
                    apiArray.put(row);

                    /* Build bean for JSP */
                    AttendanceRecord rec = new AttendanceRecord();
                    rec.setUserid(rs.getInt("user_id"));
                    rec.setLeaveType(rs.getString("leave_type"));
                    rec.setStartdate(rs.getDate("start_date"));
                    rec.setEnddate(rs.getDate("end_date"));
                    rec.setRequester(rs.getString("requester"));
                    rec.setStatus(rs.getString("status"));
                    rec.setLeavePaymentType(rs.getString("leave_payment_type"));
                    rec.setDaysTaken(rs.getInt("days_taken"));
                    jspList.add(rec);
                }

                if (isApi) {
                    json.put("status","success").put("leaveRequests", apiArray);
                    sendJson(res, HttpServletResponse.SC_OK, json);
                } else {
                    req.setAttribute("leaveRequests", jspList);
                    req.getRequestDispatcher("leavestatus.jsp").forward(req, res);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            errorJson(isApi,res,json,"DB error retrieving status");
        }
    }

    /* ===============================  HELPERS  =============================== */

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
    }

    private void sendJson(HttpServletResponse res, int status, JSONObject obj) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(obj.toString());
    }

    private void errorJson(boolean api, HttpServletResponse res,
                           JSONObject json, String msg) throws IOException {
        errorJson(api, res, json, msg, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private void errorJson(boolean api, HttpServletResponse res,
                           JSONObject json, String msg, int code) throws IOException {
        if (api) {
            json.put("status","error").put("message", msg);
            sendJson(res, code, json);
        } else {
            res.sendError(code, msg);
        }
    }
}
