<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.io.File" %>
    <%@ page import="net.infinity.db.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%	
	String fileName = request.getParameter("fileName");
	ServletContext context = getServletContext();
	String downloadPath = context.getRealPath("Upload");
	String filePath = downloadPath + "/" + fileName;
	File file = new File(filePath);
	
	file.delete();
	
	
	AttachDAO attachDAO = new AttachDAO();
	attachDAO.FileNameDelete(fileName);
	attachDAO.dbClose();
	
	String docNo = request.getParameter("docNo");
	response.sendRedirect("draftDetail.jsp?docNo="+docNo+"");
	
%>

</body>
</html>