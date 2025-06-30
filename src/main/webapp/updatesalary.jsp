<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.tcs.hr.AttendanceRecord" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Update Salary Information</title>
</head>
<body>

<%
    List<AttendanceRecord> sal = (List<AttendanceRecord>) request.getAttribute("salary");
    if (sal != null && !sal.isEmpty()) {
        for (AttendanceRecord salary : sal) {
%>
    
    <form action="updateSal" method="post">
    	<input type="hidden" name="empid" value="<%= salary.getEmpId() %>">
        <label for="role">Role</label>
        <input type="text" name="role" value="<%= salary.getRole() %>">
        <button type="submit">Update</button>
    </form>
    
<%
        }
    } 
%>

</body>
</html>
