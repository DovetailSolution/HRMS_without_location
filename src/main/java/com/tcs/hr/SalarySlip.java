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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


@WebServlet(name = "salaryslip", urlPatterns = { "/salaryslip" })
public class SalarySlip extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SalarySlip() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String empid = (String) session.getAttribute("empid");
        String slipstatus = request.getParameter("slipstatus");
        String download = request.getParameter("download");

        try {
            if ("Approved".equals(slipstatus)) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

                PreparedStatement ps = con.prepareStatement(
                    "SELECT users.empId, users.username, users.role, salary.department, salary.bank_name, " +
                    "salary.bank_account_no, salary.basic_salary, salary.dearness_allowance, salary.hra, " +
                    "salary.medical_allowance, salary.travelling_allowance " +
                    "FROM users INNER JOIN salary ON users.empId = salary.empId WHERE users.empId = ?");
                ps.setString(1, empid);
                ResultSet rs = ps.executeQuery();

                List<AttendanceRecord> records = new ArrayList<>();
                while (rs.next()) {
                    AttendanceRecord record = new AttendanceRecord();
                    record.setEmpId(rs.getString("empId"));
                    record.setUsername(rs.getString("username"));
                    record.setRole(rs.getString("role"));
                    record.setDepartment(rs.getString("department"));
                    record.setBankname(rs.getString("bank_name"));
                    record.setBankaccountno(rs.getString("bank_account_no"));
                    record.setBasicSalary(rs.getDouble("basic_salary"));
                    record.setDa(rs.getDouble("dearness_allowance"));
                    record.setHra(rs.getDouble("hra"));
                    record.setMa(rs.getDouble("medical_allowance"));
                    record.setTa(rs.getDouble("travelling_allowance"));
                    records.add(record);
                }

                if (download != null && download.equals("pdf")) {
                    generatePdf(response, records);
                } else {
                    request.setAttribute("records", records);
                    request.getRequestDispatcher("salarySlip.jsp").forward(request, response);
                }

                rs.close();
                ps.close();
                con.close();
            } 
            else {
                response.setContentType("text/html");
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h3>Your salary slip is pending for HR approval. Please check back later.</h3>");
                response.getWriter().println("</body></html>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generatePdf(HttpServletResponse response, List<AttendanceRecord> records) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=salarySlip.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Define fonts and colors
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
        Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GRAY);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
        Font regularFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
        Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        BaseColor headerColor = new BaseColor(60, 130, 200);
        BaseColor tableHeaderBg = new BaseColor(75, 75, 75);

        // Header Section
        Paragraph title = new Paragraph("Salary Slip", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        
        Paragraph monthInfo = new Paragraph("Month: October 2024", subtitleFont);
        monthInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(monthInfo);

        document.add(new Paragraph(" ")); // Add a blank line

        // Employee Details Table
        PdfPTable detailsTable = new PdfPTable(2);
        detailsTable.setWidthPercentage(100);
        detailsTable.setSpacingBefore(10f);
        detailsTable.setSpacingAfter(10f);

        addStyledCell(detailsTable, "Employee ID", boldFont, headerColor);
        addStyledCell(detailsTable, records.get(0).getEmpId(), regularFont);
        addStyledCell(detailsTable, "Name", boldFont, headerColor);
        addStyledCell(detailsTable, records.get(0).getUsername(), regularFont);
        addStyledCell(detailsTable, "Role", boldFont, headerColor);
        addStyledCell(detailsTable, records.get(0).getRole(), regularFont);
        addStyledCell(detailsTable, "Department", boldFont, headerColor);
        addStyledCell(detailsTable, records.get(0).getDepartment(), regularFont);
        addStyledCell(detailsTable, "Bank Name", boldFont, headerColor);
        addStyledCell(detailsTable, records.get(0).getBankname(), regularFont);
        addStyledCell(detailsTable, "Bank Account No", boldFont, headerColor);
        addStyledCell(detailsTable, records.get(0).getBankaccountno(), regularFont);

        document.add(detailsTable);

        // Salary Details Table with Styled Header
        PdfPTable salaryTable = new PdfPTable(2);
        salaryTable.setWidthPercentage(100);
        salaryTable.setSpacingBefore(10f);
        salaryTable.setSpacingAfter(10f);

        // Add styled header row
        PdfPCell salaryHeaderCell = new PdfPCell(new Phrase("Description", tableHeaderFont));
        salaryHeaderCell.setBackgroundColor(tableHeaderBg);
        salaryHeaderCell.setPadding(8);
        salaryTable.addCell(salaryHeaderCell);

        salaryHeaderCell = new PdfPCell(new Phrase("Amount (₹)", tableHeaderFont));
        salaryHeaderCell.setBackgroundColor(tableHeaderBg);
        salaryHeaderCell.setPadding(8);
        salaryTable.addCell(salaryHeaderCell);

        // Salary Details Rows
        addStyledCell(salaryTable, "Basic Salary", boldFont, BaseColor.LIGHT_GRAY);
        addStyledCell(salaryTable, String.valueOf(records.get(0).getBasicSalary()), regularFont);
        addStyledCell(salaryTable, "Dearness Allowance (DA)", boldFont, BaseColor.LIGHT_GRAY);
        addStyledCell(salaryTable, String.valueOf(records.get(0).getDa()), regularFont);
        addStyledCell(salaryTable, "House Rent Allowance (HRA)", boldFont, BaseColor.LIGHT_GRAY);
        addStyledCell(salaryTable, String.valueOf(records.get(0).getHra()), regularFont);
        addStyledCell(salaryTable, "Medical Allowance (MA)", boldFont, BaseColor.LIGHT_GRAY);
        addStyledCell(salaryTable, String.valueOf(records.get(0).getMa()), regularFont);
        addStyledCell(salaryTable, "Travelling Allowance (TA)", boldFont, BaseColor.LIGHT_GRAY);
        addStyledCell(salaryTable, String.valueOf(records.get(0).getTa()), regularFont);

        double totalSalary = records.get(0).getBasicSalary() + records.get(0).getDa() + records.get(0).getHra() + records.get(0).getMa() + records.get(0).getTa();
        addStyledCell(salaryTable, "Total", boldFont, BaseColor.YELLOW);
        addStyledCell(salaryTable, String.valueOf(totalSalary), boldFont);

        document.add(salaryTable);

        // Footer Section
		/*
		 * Paragraph footer = new
		 * Paragraph("This is a computer-generated document and does not require a signature."
		 * , subtitleFont); footer.setAlignment(Element.ALIGN_CENTER);
		 * document.add(footer);
		 */

        document.close();
    }

    private void addStyledCell(PdfPTable table, String text, Font font, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidth(1);
        table.addCell(cell);
    }

    private void addStyledCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidth(1);
        table.addCell(cell);
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}
