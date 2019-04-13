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
	
	//DocumentDAO docDao = new DocumentDAO();
	//List<DocumentVO> list = docDao.getPendingSendingToTeamLeader(Integer.parseInt(id));
	//docDao.dbClose();
	List<DocumentVO> list = (List<DocumentVO>)request.getAttribute("docList");
	System.out.println("documentCompleteInCEO.jsp list.size(): " + list.size());
%>

	<table>
		<tr>
			<th>문서 번호</th>
			<th>제목</th>
			<th>내용</th>
			<th>발신팀</th>
			<th>작성자</th>
			<th>완료일</th>
		</tr>
		<%
			ApprovalDAO appDao = new ApprovalDAO();
			EmpDAO empDao = new EmpDAO();
			EmpVO ceoVo = empDao.getCeoVO();
			for (DocumentVO x : list) {
				out.println("<tr><td><a href=\"documentCompleteByCEODetail.jsp?docNo=" + x.getDocNo() + "\">" + x.getDocNo()+ "</a></td><td>"
					+ x.getTitle() + "</td><td>" + StringUtils.left(x.getContent(), 20) + "</td><td>"
					+ empDao.getTeamName(x.getEmpNo()) + "</td><td>"
					+ empDao.getEmpName(x.getEmpNo()) + "</td><td>"
					+ appDao.getApprovedDate(x.getDocNo(), ceoVo.getEmpNo())
					+ "</td></tr>"
				);
			}
			empDao.dbClose();
			appDao.dbClose();
			
		%>
		
	</table>
</body>
</html>