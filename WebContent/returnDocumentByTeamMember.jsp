<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	String name = (String)session.getAttribute("name");
	String id = (String)session.getAttribute("id");
	
	if(id == null) {
		response.sendRedirect("info.html");
		return;
	}
	
	String docNo = (String)request.getAttribute("docNo");
%>
	<form action="returnDocumentByTeamMemberConfirmed.do" method="POST">
		반송사유<br>
		<textarea rows="4" cols="50" name="comment"></textarea><br>
		<input type="hidden" name="docNo" value="<%=docNo %>">
		<input type="hidden" name="id" value="<%=id %>">
		<input type="submit" value="반송">
		<button type="submit" formaction="documentPendingReceivingToTeamMember.do">취소</button>
	</form>
</body>
</html>