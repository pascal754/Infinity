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
	String name = (String)session.getAttribute("name");
	String id = (String)session.getAttribute("id");
	
	if(id == null) {
		response.sendRedirect("info.html");
		return;
	}
	//DocumentDAO docDao = new DocumentDAO();
	//List<DocumentVO> list = docDao.getPendingSendingToTeamLeader(Integer.parseInt(id));
	//docDao.dbClose();
	List<RejectedDocumentVO> list = (List<RejectedDocumentVO>)request.getAttribute("docList");
	session.setAttribute("docList", list);
	//request.setAttribute("docList", list);
	//request.getRequestDispatcher("documentRejectedDetail.jsp").forward(request, response);
	System.out.println("documentRejected.jsp list.size(): " + list.size());
%>

	<table>
		<tr>
			<th>문서 번호</th>
			<th>제목</th>
			<th>내용</th>
			<th>작성자</th>
			<th>상신일</th>
			<th>반려일</th>
		</tr>
		<%
			ApprovalDAO appDao = new ApprovalDAO();
			EmpDAO empDao = new EmpDAO();
			for (RejectedDocumentVO x : list) {
				out.println("<tr><td><a href=\"documentRejectedDetail.jsp?docNo=" + x.getDocNo() + "\">" + x.getDocNo()+ "</a></td><td>"
					+ x.getTitle() + "</td><td>" + StringUtils.left(x.getContent(), 20) + "</td><td>" + empDao.getEmpName(x.getEmpNo()) + "</td><td>"
					+ appDao.getApprovedDate(x.getDocNo(), x.getEmpNo()) + "</td><td>"
					+ x.getRejectedDate()
					+ "</td></tr>"
				);
			}
			empDao.dbClose();
			appDao.dbClose();
			
		%>
		
	</table>
</body>
</html>