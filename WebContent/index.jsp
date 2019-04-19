<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="net.infinity.db.*" %>
<%@ page import="net.infinity.action.*" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Timestamp" %>

<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.servlet.ServletException" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="javax.servlet.http.HttpServletResponse" %>
<%@ page import="javax.sql.DataSource" %>

<%@ page import="java.util.*"%>
<%@ page import="net.infinity.db.DocumentVO" %>
<%@ page import="net.infinity.db.EmpDAO" %>
<%@ page import="net.infinity.db.EmpVO" %>
<%@ page import="net.infinity.db.TeamDAO" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	session.invalidate();
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<EmpVO> list = new ArrayList<EmpVO>();

	try {
		Context init = new InitialContext();
		DataSource ds = (DataSource)init.lookup("java:comp/env/jdbc/infinity");
		conn = ds.getConnection();
		pstmt = conn.prepareStatement(
			"SELECT * from emp"
		);

		rs = pstmt.executeQuery();
		while (rs.next()) {
			EmpVO empVo = new EmpVO();
			empVo.setEmpNo(rs.getInt("emp_no"));
			empVo.setName(rs.getString("name"));
			empVo.setTeamCode(rs.getInt("team_code"));
			empVo.setTitleCode(rs.getInt("title_code"));
			empVo.setTel(rs.getInt("tel"));
			list.add(empVo);
		}
	} catch (Exception e) {
		
	} finally {
		if (pstmt != null) try { pstmt.close(); } catch (Exception e) { e.printStackTrace(); }
		if (rs != null) try { rs.close(); } catch(Exception e) { e.printStackTrace(); }
		if (conn != null) try { conn.close(); } catch(Exception e) { e.printStackTrace(); }
	}
%>
	<h1>무한발전주식회사</h1>
	<form action="loginProcess.do" method="post">
		<fieldset>
			<legend>Login</legend>
			<table border="0">
				<tr>
					<td>ID:</td>
					<td><input type="text" name="id" pattern="^[1-9]\d\d$"></td>
				</tr>
			
				<tr>
					<td>Password:</td>
					<td><input type="password" name="password"></td>
				</tr>
			
				<tr>
					<td colspan="2"><input type="submit" value="Login">
					</td>
				</tr>
			
			</table>
		</fieldset>
	</form>
	<a href="/">Galaxy MIT</a>
	<details>
		<summary>Help</summary>
		<%
			EmpDAO empDao = new EmpDAO();
			for (EmpVO x : list) {
				if (x.getTitleCode() != 4) {
					
					out.print(
						+ x.getEmpNo() + " "
						+ x.getName() + " "
						+ empDao.getTeamName(x.getEmpNo()) + " "
						+ empDao.getTitle(x.getEmpNo()) + "<br>"
						);
				}
			}
			out.print("비밀번호: 1234");
			empDao.dbClose();
		%>
	</details>
</body>
</html>