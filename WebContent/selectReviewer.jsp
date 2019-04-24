<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="net.infinity.db.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%
	String name = (String)session.getAttribute("name");
	String id = (String)session.getAttribute("id");
	
	if(id == null) {
		response.sendRedirect("info.html");
		return;
	}
	
	String docNo = (String)request.getAttribute("docNo");
	List<EmpVO> teamMembers = (List<EmpVO>)request.getAttribute("teamMembers");
%>
<style>
            #doc_title {
                position: relative;
                left: 250px;
            }

            #approval_line {
                position: relative;
                left: 300px;
            }
            table {
                border-collapse: collapse;
            }
            table.t, th.t, td.t {
                border: 1px solid gray;
            }
            td.t {
                width: 100px;
                height: 100px;
                text-align: center;
            }

            p.t {
                font-size: 40px;
                font-weight: bold;

            }


            table.c, th.c, td.c, td.d, td.e, td.f {
                border: 1px solid gray;
            }
            td.c {
                width: 150px;
                height: 30px;
            }
            td.d {
                width: 450px;
                height: 30px;
            }
            td.e {
                width: 600px;
                height: 30px;
            }
            td.f {
                width: 600px;
                height: 300px;
                vertical-align: top;
            }
        </style>
</head>
<body>

	<fieldset>
	<legend>담당자 선정</legend>
	<form action="reviewerAssigned.do" target="content">
	<%for (int i = 0; i < teamMembers.size(); ++i) { %>
		<label for="<%=i %>"><input type="radio" name="reviewer" id="<%=i %>" value="<%=teamMembers.get(i).getEmpNo() %>"><%=teamMembers.get(i).getName() %></label><br>
	<%} %>
		<input type="hidden" name="docNo" value="<%=docNo %>">
		<input type="submit" value="완료">
	</form>
	</fieldset>


</body>
</html>