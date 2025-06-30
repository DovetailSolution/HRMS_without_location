package com.tcs.hr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/downloadAttachment")
public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DownloadServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	String taskId = request.getParameter("taskId");
        
        if (taskId == null || taskId.isEmpty()) {
            response.getWriter().println("Task ID is missing.");
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            // Fetch the attachment path for the given task
            String sql = "SELECT attachment_path FROM tasks WHERE task_id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, taskId);
            rs = ps.executeQuery();

            if (rs.next()) {
                String filePath = rs.getString("attachment_path");
                File file = new File(filePath);

                if (file.exists()) {
                    // Set response headers
                    response.setContentType("application/octet-stream");
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

                    // Write file content to the response output stream
                    FileInputStream fileInputStream = new FileInputStream(file);
                    OutputStream out = response.getOutputStream();

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }

                    fileInputStream.close();
                    out.flush();
                } else {
                    response.getWriter().println("File not found.");
                }
            } else {
                response.getWriter().println("No attachment found for the given task.");
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            response.getWriter().println("Error occurred while processing the request: " + e.getMessage());
        } 
        finally 
        {
            // Close resources
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
