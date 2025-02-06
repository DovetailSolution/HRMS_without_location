package com.tcs.hr;

import java.io.IOException;


import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import io.jsonwebtoken.security.Request;

@WebServlet(name = "report", urlPatterns = { "/report" })
public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ReportServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	String action = request.getParameter("action");
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + "Report.csv\"");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            if ("download1".equals(action)) {
                ReportGenerator.generateAttendanceReport(response, con);
            } else if ("download2".equals(action)) {
                ReportGenerator.generatePivotedReport(response, con);
            } else if("download3".equals(action)) {
                ReportGenerator.generateSalaryReport(response, con);
            } 

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error generating report: " + e.getMessage());
        }
    }
}

