<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	request.setCharacterEncoding("UTF-8");
	String sessionName = (String)session.getAttribute("name");
	
	if (!sessionName.equals("momo")) {
		out.println("<script>");
		out.println("location.href='loginForm.jsp'");
		out.println("</script>");
	}

	String name = (String)request.getParameter("name");



	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	String dbName = "";
	String dbPassword = "";

	try {
		Context init = new InitialContext();
		DataSource ds = (DataSource)init.lookup("java:comp/env/jdbc/testdb");
		conn = ds.getConnection();
		pstmt = conn.prepareStatement(
				"DELETE FROM member where name=?"
				);
		pstmt.setString(1, name);
		pstmt.executeUpdate();
	
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
	
	out.println("<script>");
	out.println("location.href='memberList.jsp'");
	out.println("</script>");
%>
</body>
</html>