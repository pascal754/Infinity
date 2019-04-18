<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="net.infinity.db.*" %>
    <%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%EmpDAO empDao = new EmpDAO();
	int id = Integer.parseInt((String)session.getAttribute("id"));
	EmpVO empVO = empDao.getEmpVO(id);
	empDao.dbClose();
	%>
	<form action=PersonalpasswordReset.do method="post">
	<table>
		<tr>
			<th>아이디</th>
			<th>이름</th>
			<th>직급</th>
			<th>패스워드 리셋</th>
		<tr>
			<td><%=empVO.getEmpNo()%></td>
			<td><%=empVO.getName()%></td>
			<td><%=empVO.getTitleCode()%></td>
			<td><input type='text' name='newpass'><input type='submit' value='변경'></td>

		</tr>
</body>
</html>