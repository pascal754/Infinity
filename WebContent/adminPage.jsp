<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="net.infinity.db.*" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

</head>
	
	
<body>
<%
	EmpDAO empDao = new EmpDAO();
	String id = (String)session.getAttribute("id");
	List<EmpVO> empVoList = empDao.getAllMembers();
	
%>
	<form action=resetPassword.do method="post">
	<table>
		<tr>
			<th>아이디</th>
			<th>이름</th>
			<th>직급</th>
			<th>패스워드 리셋</th>
			<th> <input type='text' name='newpass'><input type='submit' value='초기화'></th>
			
		<%
			for (EmpVO x : empVoList) {
				out.println(
			"<tr><td>" + x.getEmpNo() + "</td><td>" + x.getName()+"</td><td>"
					+ x.getTitleCode() + "</td><td><input type='radio' name='empNo' value='"+x.getEmpNo()+"'> </td></tr>"
				);
			}	
		empDao.dbClose();
		%>
		
		
	</table>
	</form>
</body>
</html>