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
<%@ page import="net.infinity.db.SecurityUtil"%>

<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("id");
	String password = request.getParameter("password");
	
	//암호화
	SecurityUtil securityUtil = new SecurityUtil();
	String ppwd = securityUtil.encryptSHA256(password);

	
	if (id == "") {
		id = "-1";
	}
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	int dbId = 0;
	int dbTitle_code = 0;
	String dbName = "";
	String dbPassword = "";
	
	try {
		Context init = new InitialContext();
		DataSource ds = (DataSource)init.lookup("java:comp/env/jdbc/infinity");
		conn = ds.getConnection();
		pstmt = conn.prepareStatement(
				"SELECT emp_no, name, password, title_code FROM emp WHERE emp_no = ?"
				);
		pstmt.setInt(1, Integer.parseInt(id));
		rs = pstmt.executeQuery();
		
		
		
		if (rs.next()) {
			dbId = rs.getInt("emp_no");
			dbName = rs.getString("name");
			dbPassword = rs.getString("password");
			dbTitle_code = rs.getInt("title_code");
			
			
			if (Integer.parseInt(id) == dbId && ppwd.equals(dbPassword)){
				//login success and create session
				System.out.println("login successfuly");
				
				session.setAttribute("name", dbName);
				session.setAttribute("id", Integer.toString(dbId));
				session.setAttribute("title_code", dbTitle_code);
				out.println("<script>");
				out.println("location.href='main.do'");
				out.println("</script>");
			}
			
		}
		
		
		if (id.equals("-1")) {
			System.out.println("id is empty");
			out.println("<script>");
			out.println("alert('id is empty')");
			out.println("</script>");
		} 
			
		//retry login
		out.println("<script>");
		out.println("location.href='index.jsp'");
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