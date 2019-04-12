<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="java.lang.Integer" %>
<%
	String id, name, password, email, age, gender;
	//int id, age;
	
	//id = Integer.parseInt(request.getParameter("id"));
	request.setCharacterEncoding("UTF-8");
	id = request.getParameter("id");
	name = request.getParameter("name");
	password = request.getParameter("password");
	email = request.getParameter("email");
	//age = Integer.parseInt(request.getParameter("age"));
	age = request.getParameter("age");
	gender = request.getParameter("gender");
	
	
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	
	
	try {
		Context init = new InitialContext();
		DataSource ds = (DataSource)init.lookup("java:comp/env/jdbc/testdb");
		conn = ds.getConnection();
		System.out.println("success");
		System.out.println(id + name + password + email + age + gender);
		
		/*PreparedStatement pstmt = conn.prepareStatement("INSERT INTO member VALUES" +
				" (" + id + ", '" + name + "', '" + password + "', '" + email + "', " + age + ", '" + gender + "')"
		);
		*/
		pstmt = conn.prepareStatement("INSERT INTO member VALUES(?, ?, ?, ?, ?, ?)");
		//pstmt.setString(1, Integer.toString(id));
		pstmt.setString(1, id);
		pstmt.setString(2, name);
		pstmt.setString(3, password);
		pstmt.setString(4, email);
		//pstmt.setString(5, Integer.toString(age));
		pstmt.setString(5, age);
		pstmt.setString(6, gender);
		
		if (pstmt.executeUpdate() == 1) {
			System.out.println("inserted successfully");
		} else {
			System.out.println("inser failed");
		}
		
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("DB connection failed");
	} finally {
		try {
			pstmt.close();
			conn.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
%>