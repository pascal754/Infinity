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
<%
	String name = (String)session.getAttribute("name");
	String id = (String)session.getAttribute("id");
	
	//DocumentDAO docDao = new DocumentDAO();
	//List<DocumentVO> list = docDao.getPendingSendingToTeamLeader(Integer.parseInt(id));
	//docDao.dbClose();
	List<DocumentVO> list = (List<DocumentVO>)request.getAttribute("docList");
	System.out.println("PendingSendingToCEO.jsp list.size(): " + list.size());
%>

	<table>
		<tr>
			<th>문서 번호</th>
			<th>제목</th>
			<th>내용</th>
			<th>상신일</th>
		</tr>
		<%
			ApprovalDAO appDao = new ApprovalDAO();
			EmpDAO empDao = new EmpDAO();
			for (DocumentVO x : list) {
				out.println("<tr><td><a href=\"pendingSendingToCEODetail.jsp?docNo=" + x.getDocNo() + "\">" + x.getDocNo()+ "</a></td><td>"
					 + x.getTitle() + "</td><td>" + x.getContent() + "</td><td>" + appDao.getApprovedDate(x.getDocNo(), empDao.getTeamLeaderNoFromEmpNo(x.getEmpNo()))
					 + "</td></tr>"
				);
			}
			empDao.dbClose();
			appDao.dbClose();
			
		%>
		
	</table>
</body>
</html>