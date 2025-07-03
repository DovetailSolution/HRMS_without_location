package com.tcs.hr;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import io.jsonwebtoken.*;
import org.json.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

/**
 * Attendance servlet — sign-in, sign-out, daily status, monthly PDF.
 */
@WebServlet(name = "attendance", urlPatterns = "/attendance")
public class AttendanceServlet extends HttpServlet {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /* ====================================================  GET  ==================================================== */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Claims claims = authenticate(req, res);
        if (claims == null) return;                       // 401 already sent

        String empId  = claims.get("empid", String.class);
        boolean isApi = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
        JSONObject json = new JSONObject();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT u.username, a.* FROM users u " +
                     "JOIN attendance a ON u.empId = a.empId WHERE u.empId = ?")) {

            ps.setString(1, empId);

            try (ResultSet rs = ps.executeQuery()) {
                JSONArray records = new JSONArray();

                while (rs.next()) {
                    JSONObject rec = new JSONObject();
                    Timestamp in  = rs.getTimestamp("sign_in_time");
                    Timestamp out = rs.getTimestamp("sign_out_time");

                    rec.put("username",             rs.getString("username"));
                    rec.put("sign_in_time",         in  != null ? in.toString()  : JSONObject.NULL);
                    rec.put("sign_out_time",        out != null ? out.toString() : JSONObject.NULL);
                    rec.put("sign_in_location",     rs.getString("location"));
                    rec.put("sign_out_location",    rs.getString("sign_out_location"));
                    records.put(rec);
                }

                if (isApi) {
                    json.put("status","success").put("attendance",records);
                    sendJson(res, HttpServletResponse.SC_OK, json);
                } else {
                    req.setAttribute("attendanceRecords", records);
                    req.getRequestDispatcher("attendance.jsp").forward(req, res);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            errorJson(isApi, res, json, "DB error");
        }
    }

    /* ====================================================  POST  =================================================== */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Claims claims = authenticate(req, res);
        if (claims == null) return;                       // 401 already sent

        String user   = claims.getSubject();
        String empId  = claims.get("empid", String.class);
        String action = req.getParameter("action");
        boolean isApi = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
        JSONObject json = new JSONObject();

        switch (action) {
            case "signin":
                signIn(req, res, isApi, json, user, empId);
                break;
            case "signout":
                signOut(req, res, isApi, json, user);
                break;
            case "viewstatus":
                viewStatus(req, res, isApi, json, user);
                break;
            case "downloadReport":
                downloadReport(req, res, isApi, json, user);
                break;
            default:
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    /* ======================================  ACTION HANDLERS  ===================================== */

    private void signIn(HttpServletRequest req, HttpServletResponse res,
                        boolean isApi, JSONObject json,
                        String user, String empId) throws IOException {

        String lat = req.getParameter("latitude");
        String lon = req.getParameter("longitude");
        String location = fetchLocation(lat, lon);

        ZoneId zone = ZoneId.of("Asia/Kolkata");
        String signInTime = FMT.format(LocalDateTime.now(zone));

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO attendance (user_id, empId, sign_in_time, location, latitude, longitude) " +
                     "VALUES ((SELECT id FROM users WHERE username=?), ?, ?, ?, ?, ?)")) {

            ps.setString(1, user);
            ps.setString(2, empId);
            ps.setString(3, signInTime);
            ps.setString(4, location);
            ps.setString(5, lat);
            ps.setString(6, lon);
            ps.executeUpdate();

            /* update session attributes for JSP */
            HttpSession s = req.getSession();
            s.setAttribute("signedIn",          true);
            s.setAttribute("lastSignInTime",    signInTime);
            s.setAttribute("lastSignOutTime",   null);
            s.setAttribute("location",          location);

            if (isApi) {
                json.put("status","success")
                    .put("message","Sign-in successful")
                    .put("signInTime", signInTime)
                    .put("location",   location);
                sendJson(res, HttpServletResponse.SC_OK, json);
            } else {
                res.sendRedirect("dashboard.jsp");      // refresh page
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            errorJson(isApi, res, json, "DB error on sign-in");
        }
    }

    private void signOut(HttpServletRequest req, HttpServletResponse res,
                         boolean isApi, JSONObject json,
                         String user) throws IOException {

        String lat = req.getParameter("latitude");
        String lon = req.getParameter("longitude");
        String location = fetchLocation(lat, lon);

        ZoneId zone = ZoneId.of("Asia/Kolkata");
        String signOutTime = FMT.format(LocalDateTime.now(zone));

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE attendance SET sign_out_time=?, sign_out_location=?, " +
                     "sign_out_latitude=?, sign_out_longitude=? " +
                     "WHERE user_id=(SELECT id FROM users WHERE username=?) AND sign_out_time IS NULL")) {

            ps.setString(1, signOutTime);
            ps.setString(2, location);
            ps.setString(3, lat);
            ps.setString(4, lon);
            ps.setString(5, user);
            ps.executeUpdate();

            /* update session attributes */
            HttpSession s = req.getSession();
            s.setAttribute("signedIn",       false);
            s.setAttribute("lastSignOutTime", signOutTime);

            if (isApi) {
                json.put("status","success")
                    .put("message","Sign-out successful")
                    .put("signOutTime", signOutTime)
                    .put("location",    location);
                sendJson(res, HttpServletResponse.SC_OK, json);
            } else {
                res.sendRedirect("dashboard.jsp");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            errorJson(isApi, res, json, "DB error on sign-out");
        }
    }

    private void viewStatus(HttpServletRequest req, HttpServletResponse res,
            boolean isApi, JSONObject json, String user)
throws IOException, ServletException {

LocalDateTime start = LocalDate.now().atStartOfDay();
LocalDateTime end   = LocalDate.now().atTime(LocalTime.MAX);

try (Connection con = getConnection();
PreparedStatement ps = con.prepareStatement(
"SELECT sign_in_time, sign_out_time, location, sign_out_location " +
"FROM attendance WHERE user_id=(SELECT id FROM users WHERE username=?) " +
"AND sign_in_time BETWEEN ? AND ?")) {

ps.setString(1, user);
ps.setTimestamp(2, Timestamp.valueOf(start));
ps.setTimestamp(3, Timestamp.valueOf(end));

try (ResultSet rs = ps.executeQuery()) {

JSONArray     arr   = new JSONArray();            // for API
List<AttendanceRecord> list = new ArrayList<>();  // for JSP
long totalMs = 0;

while (rs.next()) {
    Timestamp in  = rs.getTimestamp("sign_in_time");
    Timestamp out = rs.getTimestamp("sign_out_time");

    // build JSON obj for API
    arr.put(new JSONObject()
        .put("signInTime",  in  != null ? in.toString()  : JSONObject.NULL)
        .put("signOutTime", out != null ? out.toString() : JSONObject.NULL)
        .put("location",        rs.getString("location"))
        .put("signOutLocation", rs.getString("sign_out_location")));

    // build Java bean for JSP
    AttendanceRecord rec = new AttendanceRecord();
    rec.setSignInTime(in);
    rec.setSignOutTime(out);
    rec.setLocation(rs.getString("location"));
    rec.setSignOutLocation(rs.getString("sign_out_location"));
    list.add(rec);

    if (in != null && out != null) totalMs += out.getTime() - in.getTime();
}

String totalHms = formatHMS(totalMs);

if (isApi) {
    json.put("status","success")
        .put("attendanceRecords", arr)
        .put("totalDailyTime", totalHms);
    sendJson(res, HttpServletResponse.SC_OK, json);
} else {
    req.setAttribute("attendanceRecords", list);
    req.setAttribute("totalDailyTime", totalHms);
    req.getRequestDispatcher("status.jsp").forward(req, res);
}
}
} catch (Exception ex) {
ex.printStackTrace();
errorJson(isApi, res, json, "DB error on viewStatus");
}
}


    private void downloadReport(HttpServletRequest req, HttpServletResponse res,
                                boolean isApi, JSONObject json,
                                String user) throws IOException {

        String yearMonth = req.getParameter("yearMonth");
        if (yearMonth == null || yearMonth.isBlank()) {
            errorJson(isApi, res, json, "yearMonth parameter missing",
                      HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        YearMonth ym = YearMonth.parse(yearMonth);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end   = ym.atEndOfMonth().atTime(LocalTime.MAX);

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                "SELECT sign_in_time, sign_out_time, location, sign_out_location " +
                "FROM attendance WHERE user_id=(SELECT id FROM users WHERE username=?) " +
                "AND sign_in_time BETWEEN ? AND ?")) {

            ps.setString(1, user);
            ps.setTimestamp(2, Timestamp.valueOf(start));
            ps.setTimestamp(3, Timestamp.valueOf(end));

            try (ResultSet rs = ps.executeQuery()) {

                res.setContentType("application/pdf");
                res.setHeader("Content-Disposition",
                              "attachment; filename=attendance_report_" + yearMonth + ".pdf");

                Document doc = new Document();
                PdfWriter.getInstance(doc, res.getOutputStream());
                doc.open();

                Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
                Paragraph title = new Paragraph("Attendance Report - " + yearMonth, titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                doc.add(title);
                doc.add(new Paragraph("\n"));

                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                table.addCell("Sign In");
                table.addCell("Sign Out");
                table.addCell("Sign In Location");
                table.addCell("Sign Out Location");

                while (rs.next()) {
                    Timestamp in  = rs.getTimestamp("sign_in_time");
                    Timestamp out = rs.getTimestamp("sign_out_time");
                    table.addCell(in  != null ? FMT.format(in.toLocalDateTime())  : "");
                    table.addCell(out != null ? FMT.format(out.toLocalDateTime()) : "");
                    table.addCell(rs.getString("location"));
                    table.addCell(rs.getString("sign_out_location"));
                }
                doc.add(table);
                doc.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            errorJson(isApi, res, json, "Error generating PDF");
        }
    }

    /* =====================================  COMMON HELPERS  ====================================== */

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/attendance_db", "root", "manager");
    }

    private Claims authenticate(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Optional<String> opt = JwtUtil.extractToken(req);
        if (opt.isEmpty()) { sendUnauth(res); return null; }

        try { return JwtUtil.verify(opt.get()).getBody(); }
        catch (JwtException ex) { sendUnauth(res); return null; }
    }

    private void sendJson(HttpServletResponse res, int status, JSONObject obj) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(obj.toString());
    }

    private void sendUnauth(HttpServletResponse res) throws IOException {
        sendJson(res, HttpServletResponse.SC_UNAUTHORIZED,
                 new JSONObject().put("status","error").put("message","Missing or invalid token"));
    }

    private void errorJson(boolean isApi, HttpServletResponse res, JSONObject json,
                           String msg) throws IOException {
        errorJson(isApi, res, json, msg, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private void errorJson(boolean isApi, HttpServletResponse res, JSONObject json,
                           String msg, int code) throws IOException {
        if (isApi) {
            json.put("status","error").put("message", msg);
            sendJson(res, code, json);
        } else {
            res.sendError(code, msg);
        }
    }

    private String formatHMS(long ms) {
        long s = ms / 1000, h = s / 3600, m = (s % 3600) / 60, sec = s % 60;
        return String.format("%02d:%02d:%02d", h, m, sec);
    }

    /* Replace with real reverse-geocoding if desired */
    private String fetchLocation(String lat, String lon) { return "Unknown"; }
}
