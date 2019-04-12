<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="net.infinity.db.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String name = (String)session.getAttribute("name");
	String id = (String)session.getAttribute("id");
	
	DocumentVO docVo = (DocumentVO)request.getAttribute("docVo");
	String[] teams = (String [])request.getAttribute("teams");
	String approvalLine = (String)request.getAttribute("approvalLine");
	//List<String> allTeams = (List<String>)request.getAttribute("allTeams");
	EmpVO empVo = (EmpVO)request.getAttribute("empVo");
	EmpDAO empDao = new EmpDAO();
	System.out.println("**savedDocument.jsp**");
	System.out.println("writer emp no: " + empVo.getEmpNo());
	System.out.println("team leader no: " + empDao.getTeamLeaderNoFromEmpNo(empVo.getEmpNo()));
	EmpVO teamLeaderVo = empDao.getEmpVO(empDao.getTeamLeaderNoFromEmpNo(empVo.getEmpNo()));
	EmpVO ceoVo = empDao.getCeoVO();
	String teamName = empDao.getTeamName(empVo.getEmpNo());
	System.out.println("team name: " + teamName);
	
	List<String> allTeams = empDao.getAllTeams();
	
	allTeams.remove(empDao.getTeamName(Integer.parseInt(id)));
	System.out.println("all teams: ");
	for (String x : allTeams) {
		System.out.println(x);
	}
	empDao.dbClose();
	
		
	
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Document</title>
<style>
            #doc_title {
                position: relative;
                left: 250px;
            }

            #approval_line {
                position: relative;
                <%if (approvalLine.equals("teamLeader")) {%>
                	left: 400px;
                <%} else {%>
                	left: 300px;
                <%} %>
                
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
<form action="saveDocument" method="post">
<div id ="doc_title">
                <p class="t">결재문서</p>
            </div>

            <div id="approval_line">
                <table class="t">
                    <tr class="t">
                        <td class="t">작성자<br><%=empVo.getName() %><br> / / </td>
                        <td class="t">팀장<br><%=teamLeaderVo.getName() %><br> / / </td>
                        <%if (approvalLine.equals("ceo")) {%>
                        <td class="t">사장<br><%=ceoVo.getName() %><br> / / </td>
                        <%}%>
                    </tr>
                </table>
            </div>
            <br clear=right>
            <br>
            <br>
            <br>
            <table>
                <tr class="c">
                    <td class="c">팀명</td>
                    <td class="d"><%=teamName %></td>
                </tr>
                <tr class="c">
                    <td class="c">작성자</td>
                    <td class="d"><%=empVo.getName() %></td>
                </tr>
                <tr class="c">
                    <td class="c">TEL</td>
                    <td class="d"><%=empVo.getTel() %></td>
                </tr>
                <tr class="c">
                    <td class="c">문서 번호</td>
                    <td class="d"><%=docVo.getDocNo() %></td>
                </tr>
                <tr class="c">
                    <td class="c">수신처</td>
                    <td class="d">
                        <%
                        	int i = 0;
                        	for (i = 0; i < allTeams.size() - 1; ++i) {
                        		out.print(allTeams.get(i) + "<input type=\"checkbox\" name=\"teams\" value=\"" + allTeams.get(i) + "\"");
                        		if (Arrays.asList(teams).contains(allTeams.get(i)))
                        			out.println("checked=\"checked\"");
                        		out.println(">");
                        	}
                        	out.print(allTeams.get(i) + "<input type=\"checkbox\" name=\"teams\" value=\"" + allTeams.get(i) + "\"");
                        	if (Arrays.asList(teams).contains(allTeams.get(i)))
                    			out.println("checked=\"checked\"");
                    		out.println(">");
                        %>
                        </td>
                </tr>
                <tr class="c">
                    <td class="c">제목</td>
                    <td class="d"><textarea name="title" id="title" required cols="50" rows="1"><%=docVo.getTitle() %></textarea></td>
                </tr>
            </table>
			<br>
			<br>
            <br>
            <table>
                <tr class="c">
                    <td class="e">내용</td>
                </tr>
                <tr class="c">
                    <td class="f"><textarea name="content" id="content" required cols="80" rows="20"><%=docVo.getContent() %></textarea></td>
                </tr>
            </table>
            <br>
            <br>
            <table>
                <tr class="c">
                    <td class="e">첨부</td>
                </tr>
                <tr class="c">
                    <td class="e">&nbsp;</td>
                </tr>
            </table>
            
  
            <input type="hidden" name="emp_no" value="<%=id %>">
            <input type="hidden" name="doc_no" value="<%=docVo.getDocNo() %>">
            <input type="hidden" name="startTime" value="<%=docVo.getStartTime() %>">
            <input type ="hidden" name="approvalLine" value="<%=approvalLine %>">
			<input type="submit" value="저장">
			<a href="report.jsp?docNo=<%=docVo.getDocNo() %>"><button type="button">상신</button></a>

</form>
</body>
</html>