<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	

<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>

<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("id");
	String password = request.getParameter("password");
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	int dbId = 0;
	String dbName = "";
	String dbPassword = "";
	
	try {
		Context init = new InitialContext();
		DataSource ds = (DataSource)init.lookup("java:comp/env/jdbc/infinity");
		conn = ds.getConnection();
		pstmt = conn.prepareStatement(
				"SELECT emp_no, name, password FROM emp WHERE emp_no=?"
				);
		pstmt.setInt(1, Integer.parseInt(id));
		rs = pstmt.executeQuery();
		
		
		
		while (rs.next()) {
			dbId = rs.getInt("emp_no");
			dbName = rs.getString("name");
			dbPassword = rs.getString("password");
		}
		
		
		if (id.equals("")) {
			System.out.println("id is empty");
			out.println("<script>");
			out.println("alert('id is empty')");
			out.println("</script>");
		} else if (Integer.parseInt(id) != dbId) {
			System.out.println("id is not correct");
			out.println("<script>");
			out.println("alert('id is not correct')");
			out.println("</script>");
		} else if (!password.equals(dbPassword)) {
			System.out.println("password is not correct");
			out.println("<script>");
			out.println("alert('password is not correct')");
			out.println("</script>");
		} else {
			//login success and create session
			System.out.println("login successfuly");
			
			session.setAttribute("name", dbName);
			session.setAttribute("id", Integer.toString(dbId));
			out.println("<script>");
			out.println("location.href='main.jsp'");
			out.println("</script>");
		}
		//retry login
		out.println("<script>");
		out.println("location.href='loginForm.jsp'");
		out.println("</script>");
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("DB connection failed");
	} finally {
		try {
			rs.close();
			pstmt.close();
			conn.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	} //end of first try-catch-finally
	
%>
</body>
</html>