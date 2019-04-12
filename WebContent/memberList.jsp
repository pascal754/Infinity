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
	String name = (String)session.getAttribute("name");
	
	if (!name.equals("momo")) {
		out.println("<script>");
		out.println("location.href='loginForm.jsp'");
		out.println("</script>");
	}
	
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
				"SELECT name FROM member"
				);
		rs = pstmt.executeQuery();
		while (rs.next()) {
%>
			<a href="memberInfo.jsp?name=<%=rs.getString("name")%>"><%=rs.getString("name")%></a>&nbsp;&nbsp;
		
			<a href="memberDelete.jsp?name=<%=rs.getString("name")%>">삭제</a><br>
					
<%
		}
	
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
	<a href="main.jsp">Main 회면</a>
	
</body>
</html>