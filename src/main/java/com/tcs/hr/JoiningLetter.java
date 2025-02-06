package com.tcs.hr;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@WebServlet(name = "joining", urlPatterns = { "/joining" })
public class JoiningLetter extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public JoiningLetter() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Optionally handle GET request if needed
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Retrieve data from form submission
        String employeeName = request.getParameter("employeeName");
        String address = request.getParameter("address");
        String position = request.getParameter("position");
        String location = request.getParameter("location");
        String date = request.getParameter("date");
        
        // Set PDF response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"Joining_Letter.pdf\"");
        
        // Create PDF content
        Document document = new Document();
        try (OutputStream out = response.getOutputStream()) {
            PdfWriter.getInstance(document, out);
            document.open();
            
            // Write content to the PDF
            document.add(new Paragraph("                                                Joining Letter"));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(employeeName));
            document.add(new Paragraph(address));
            document.add(new Paragraph(" ")); 
            document.add(new Paragraph("Dear " + employeeName + ","));
            document.add(new Paragraph("We are pleased to offer you the position of " + position + " with DOVETAIL SOLUTIONS."));
            document.add(new Paragraph("Your location will be " + location + ", and you are required to join on " + date + ", failing which the offer may be withdrawn."));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Terms & Conditions"));
            document.add(new Paragraph("1. Probation Period: You will be on probation for a period of three months from the date of your appointment. If not confirmed, your probation period will stand extended until further notice."));
            document.add(new Paragraph("2. Hours of Work: The company observes a six-day workweek from Monday to Saturday, with timings from 9:30 AM to 6:30 PM. These hours are subject to change based on business needs."));
            document.add(new Paragraph("3. Document Submission: On the day of joining, please report to the HR Manager with the following documents:"));
            document.add(new Paragraph("   a) Two passport-sized color photographs"));
            document.add(new Paragraph("   b) Last salary slip & relieving letter from previous employer"));
            document.add(new Paragraph("   c) Scanned copies of all educational documents"));
            document.add(new Paragraph("   d) Proof of residence and date of birth"));
            document.add(new Paragraph("   e) Copy of PAN card and Aadhaar card"));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("We welcome you to our team and wish you a successful career with us."));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Best Regards"));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Dovetail Solutions"));
            
            document.close();
            
            // Flush the output stream to complete the response
            out.flush();
            
        } 
        catch (DocumentException e) 
        {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Document generation error.");
        }
    }
}
