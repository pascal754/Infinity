<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="net.infinity.db.*" %>
    <%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%
	String id = (String)session.getAttribute("id");
	if(id == null) {
		response.sendRedirect("info.html");
		return;
	}
%>
</head>
<body>
	<%EmpDAO empDao = new EmpDAO();
	int emp_no = Integer.parseInt((String)session.getAttribute("id"));
	EmpVO empVO = empDao.getEmpVO(emp_no);
	
	%>
	<form action=PersonalpasswordReset.do method="post">
	<p>관리 정책에 따라 패스워드는 1234로 다시 초기화 됩니다.</p>
	<table>
		<tr>
			<th>아이디</th>
			<th>이름</th>
			<th>직급</th>
			<th>패스워드 변경</th>
		<tr>
			<td><%=empVO.getEmpNo()%></td>
			<td><%=empVO.getName()%></td>
			<td><%=empDao.getTitle(empVO.getEmpNo())%></td>
			<%empDao.dbClose(); %>
			<td><input type='text' name='newpass'><input type='submit' value='변경'></td>
			

		</tr>
	</table>
</body>
</html>