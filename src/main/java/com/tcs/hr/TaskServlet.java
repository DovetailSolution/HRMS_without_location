package com.tcs.hr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "task", urlPatterns = { "/task" })
public class TaskServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public TaskServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Check if this is an API request
        boolean isApiRequest = "XMLHttpRequest".equals(request.getHeader("task"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject jsonResponse = new JSONObject();
        JSONArray tasksArray = new JSONArray();

        try {
            HttpSession session = request.getSession();
            String empId = (String) session.getAttribute("empid");

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            String sql = "SELECT * FROM tasks WHERE assigned_to = ? ORDER BY priority DESC, due_date ASC";
            ps = con.prepareStatement(sql);
            ps.setString(1, empId);
            rs = ps.executeQuery();

            List<Map<String, Object>> tasks = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> task = new HashMap<>();
                task.put("taskId", rs.getInt("task_id"));
                task.put("taskName", rs.getString("task_name"));
                task.put("description", rs.getString("task_description"));
                task.put("dueDate", rs.getDate("due_date"));
                task.put("priority", rs.getString("priority"));
                task.put("status", rs.getString("status"));
                task.put("attachmentPath", rs.getString("attachment_path"));
                tasks.add(task);

                // Add task to JSON array if it's an API request
                if (isApiRequest) {
                    JSONObject taskJson = new JSONObject(task);
                    tasksArray.put(taskJson);
                }
            }

            if (isApiRequest) {
                // Return JSON response for API requests
                jsonResponse.put("tasks", tasksArray);
                response.getWriter().write(jsonResponse.toString());
            } else {
                // Forward tasks to JSP for non-API requests
                request.setAttribute("tasks", tasks);
                request.getRequestDispatcher("task.jsp").forward(request, response);
            }

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            if (isApiRequest) 
            {
                // Return error as JSON for API requests
                jsonResponse.put("error", "Error retrieving tasks: " + e.getMessage());
                response.getWriter().write(jsonResponse.toString());
            } 
            else 
            {
                // Forward error to JSP for non-API requests
                request.setAttribute("error", "Error retrieving tasks: " + e.getMessage());
                request.getRequestDispatcher("task.jsp").forward(request, response);
            }
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            HttpSession session = request.getSession();
            String remarks = request.getParameter("remark");
            String taskId = request.getParameter("taskId"); // Task ID to identify the task

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager");

            // Update the remarks for the specific task
            String sql = "UPDATE tasks SET remarks = ? WHERE task_id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, remarks);
            ps.setString(2, taskId);

            ps.executeUpdate();

            response.sendRedirect("task");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error updating remarks: " + e.getMessage());
            request.getRequestDispatcher("task.jsp").forward(request, response);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
