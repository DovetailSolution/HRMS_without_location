package com.tcs.hr;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// You will need a PDF library, such as iText, to generate the PDF content
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@WebServlet(name = "relieving", urlPatterns = { "/relieving" })
public class RelievingLetter extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RelievingLetter() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form data
        String employeeName = request.getParameter("employeeName");
        String position = request.getParameter("position");
        String relievingDate = request.getParameter("relievingDate");

        // Set up PDF response
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"Relieving_Letter.pdf\"");

        // Create PDF content
        Document document = new Document();
        try (OutputStream out = response.getOutputStream()) {
            PdfWriter.getInstance(document, out);
            document.open();

            // Write content
            document.add(new Paragraph("Relieving Letter"));
            document.add(new Paragraph("Date: " + relievingDate));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("To,"));
            document.add(new Paragraph("Mr./Mrs." + employeeName));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Subject: Relieving Letter"));
            document.add(new Paragraph("Dear Sir/Madam,"));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("This is to certify that " + employeeName + ", who was working as " + position +
                    " in our company, has been relieved from their duties effective " + relievingDate + "."));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("We appreciate their contributions during their tenure and wish them all the best in future endeavors."));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Thank you,"));
            document.add(new Paragraph("Dovetail Solutions"));

            document.close();
        } 
        catch (DocumentException e) 
        {
            e.printStackTrace();
        }
    }
}
