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
	DocumentDAO docDao = new DocumentDAO();
	String id = (String)session.getAttribute("id");
	List<DocumentVO> docVoList = docDao.getDocumentBeingSent(Integer.parseInt(id));
	docDao.dbClose();
%>

	<table>
		<tr>
			<th>문서 번호</th>
			<th>제목</th>
			<th>내용</th>
			<th>작성자</th>
			<th>상신일</th>
		<%
			ApprovalDAO appDao = new ApprovalDAO();
			EmpDAO empDao = new EmpDAO();
			for (DocumentVO x : docVoList) {
				out.println("<tr><td><a href=\"documentBeingSentDetail.jsp?docNo=" + x.getDocNo() + "\">" + x.getDocNo()+ "</a></td><td>"
					+ x.getTitle() + "</td><td>" + StringUtils.left(x.getContent(), 20) + "</td><td>" + empDao.getEmpName(x.getEmpNo()) + "</td><td>"
					+ appDao.getApprovedDate(x.getDocNo(), Integer.parseInt(id)) + "</td></tr>"
				);
			}
			empDao.dbClose();
			appDao.dbClose();
		%>
		
	</table>
</body>
</html>
