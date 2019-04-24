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
<%
	String id = (String)session.getAttribute("id");
	System.out.println(id);
	if(id == null) {
		System.out.println("trying redirect");
		//out.println("<script>location.href='index.jsp';</script>");
		
		response.sendRedirect("info.html");
		return;
	}
%>
</head>
<body>
<%
	DocumentDAO docDao = new DocumentDAO();

	List<DocumentVO> docVoList = docDao.getDraft(Integer.parseInt(id));
	docDao.dbClose();
%>

	<table>
		<tr>
			<th>문서 번호</th>
			<th>제목</th>
			<th>내용</th>
			<th>작성자</th>
			<th>저장시각</th>
		</tr>
		<%
			EmpDAO empDao = new EmpDAO();
			for (DocumentVO x : docVoList) {
				out.println("<tr><td><a href=\"draftDetail.jsp?docNo=" + x.getDocNo() + "\">" + x.getDocNo()+ "</a></td><td>"
					+ x.getTitle() + "</td><td>" + StringUtils.left(x.getContent(), 20) + "</td><td>" + empDao.getEmpName(x.getEmpNo()) + "</td><td>"
					+ x.getSaveTime() + "</td></tr>"
				);
			}
			empDao.dbClose();
		%>
		
	</table>
</body>
</html>
