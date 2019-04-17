<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="net.infinity.db.*" %>
 <%@ page import="java.io.File" %>
 <%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	String docNo = request.getParameter("docNo");
	DocumentDAO docDao = new DocumentDAO();
	docDao.deleteDocument(docNo);
	docDao.dbClose();
	
	AttachDAO attachDAO = new AttachDAO();
	List<String> getfilename = attachDAO.getFilename(docNo);
	
	for(String filenames : getfilename){
		String fileName = filenames;
		ServletContext context = getServletContext();
		String downloadPath = context.getRealPath("Upload");
		String filePath = downloadPath + "/" + fileName;
		File file = new File(filePath);
		
		file.delete();
	}
	
	attachDAO.FileDelete(docNo);
	attachDAO.dbClose();
	
	response.sendRedirect("draft.jsp");
%>
</body>
</html>