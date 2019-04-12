<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Main Page</h1>
<%
	String name = (String)session.getAttribute("name");
	String id = (String)session.getAttribute("id");
	


%>
	Welcome
	
	<%=name %>
	
	<a href="approvalLine.jsp">결재문서 작성</a>
	
<%
	if (name.equals("momo")) {
%>
	<br>관리자님 반갑습니다.<br>
	<a href="memberList.jsp">회원 목록 보기</a>
<%	
	}
%>
	
	<a href="logoutProcess.jsp">Logout</a>
	
</body>
</html>