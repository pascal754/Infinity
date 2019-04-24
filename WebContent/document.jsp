<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.infinity.db.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String name = (String)session.getAttribute("name");
	String id = (String)session.getAttribute("id");
	
	if(id == null) {
		response.sendRedirect("info.html");
		return;
	}
	
	String approvalLine = "";
	approvalLine = request.getParameter("approvalLine");
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;
	PreparedStatement pstmt4 = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	ResultSet rs3 = null;
	ResultSet rs4 = null;
	
	int teamCode = 0;
	int tel = 0;
	String teamName = "";
	String teamLeader = "";
	String ceo = "";
	
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String startTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(timestamp);
    String documentNo = (new SimpleDateFormat("yyyyMMddHHmmss")).format(timestamp);
    
    
	
	try {
		Context init = new InitialContext();
		DataSource ds = (DataSource)init.lookup("java:comp/env/jdbc/infinity");
		conn = ds.getConnection();
		pstmt = conn.prepareStatement(
				"SELECT team_code, tel FROM emp WHERE emp_no=?"
				);
		pstmt.setInt(1, Integer.parseInt(id));
		rs = pstmt.executeQuery();
		
		
		
		if (rs.next()) {
			teamCode = rs.getInt("team_code");
			tel = rs.getInt("tel");
		}
		
		pstmt2 = conn.prepareStatement(
				"SELECT team_name FROM team WHERE team_code=?"
				);
		pstmt2.setInt(1, teamCode);
		rs2 = pstmt2.executeQuery();
		
		
		
		if (rs2.next()) {
			teamName = rs2.getString("team_name");
		}
		
		pstmt3 = conn.prepareStatement(
				"SELECT name FROM emp WHERE title_code=2 AND team_code=?"
				);
		pstmt3.setInt(1, teamCode);
		rs3 = pstmt3.executeQuery();
		
		if (rs3.next()) {
			teamLeader = rs3.getString("name");
		}
		
		pstmt4 = conn.prepareStatement(
				"SELECT name FROM emp WHERE title_code=3"
				);
		rs4 = pstmt4.executeQuery();
		
		if (rs4.next()) {
			ceo = rs4.getString("name");
		}
		
		System.out.println("팀장: " + teamLeader);
		System.out.println("사장: " + ceo);
		documentNo = teamCode + "-" + id + "-" + documentNo;
		System.out.println(documentNo);
		System.out.println(startTime);
		
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("DB connection failed");
	} finally {
		try {
			rs.close();
			rs2.close();
			pstmt.close();
			conn.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	} //end of first try-catch-finally
	
	
	EmpDAO empDao = new EmpDAO();
	List<String> teams = empDao.getAllTeams();
	
	System.out.println("**document.jsp**");
	System.out.println("all teams:");
	
	teams.remove(empDao.getTeamName(Integer.parseInt(id)));
	if (teams != null)
		for (String x : teams) {
			System.out.println(x);
		}
	empDao.dbClose();

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
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
<script>
	jQuery(function ($) {
	    //form submit handler
	    $('#document').submit(function (e) {
	        //check at leat 1 checkbox is checked
	        if (!$('.team').is(':checked')) {
	            //prevent the default form submit if it is not checked
	            e.preventDefault();
	            alert('팀을 선택하세요.');
	        }
	    })
	})
</script>
<form action="saveDocument.do?foldername=<%=documentNo %>" id="document" method="post" enctype="multipart/form-data">
<div id ="doc_title">
                <p class="t">결재문서</p>
            </div>

            <div id="approval_line">
                <table class="t">
                    <tr class="t">
                        <td class="t">작성자<br><%=name %><br> / / </td>
                        <td class="t">팀장<br><%=teamLeader %><br> / / </td>
                        <%if (approvalLine.equals("ceo")) {%>
                        <td class="t">사장<br><%=ceo %><br> / / </td>
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
                    <td class="d"><%=name %></td>
                </tr>
                <tr class="c">
                    <td class="c">TEL</td>
                    <td class="d"><%=tel %></td>
                </tr>
                <tr class="c">
                    <td class="c">문서 번호</td>
                    <td class="d"><%=documentNo %></td>
                </tr>
                <tr class="c">
                    <td class="c">수신처</td>
                    	<td class="d">
                    
						<div class="form-group options">
                        <%
                        	if (teams.size() != 0) {
	                        	int i = 0;
	                        	for (i = 0; i < teams.size() - 1; ++i) {
	                        		out.println("<label for=\"" + i + "\"><input type=\"checkbox\" name=\"teams\" class=\"team\" value=\"" + teams.get(i) + "\" id=\"" + i + "\">" +teams.get(i) + "</label>,");
	                        	}
                        		out.println("<label for=\"" + i + "\"><input type=\"checkbox\" name=\"teams\" class=\"team\" value=\"" + teams.get(i) + "\" id=\"" + i + "\">" +teams.get(i) + "</label>");
                        	}
                        %>
                        </div>
                        </td>
                </tr>
                <tr class="c">
                    <td class="c">제목</td>
                    <td class="d"><textarea name="title" id="title" cols="50" rows="1" required></textarea></td>
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
                    <td class="f"><textarea name="content" id="content" cols="80" rows="20" required></textarea></td>
                </tr>
            </table>
            <br>
            <br>
            <table>
                <tr class="c">
                    <td class="e">첨부 <input type="file" name="filename"></td>
                </tr>
            </table>
            <input type="hidden" name="emp_no" value="<%=id %>">
            <input type="hidden" name="doc_no" value="<%=documentNo %>">
            <input type="hidden" name="startTime" value="<%=startTime %>">
            <input type ="hidden" name="approvalLine" value="<%=approvalLine %>">
			<input type="submit" value="저장">
			<button type="submit" formaction="report.jsp?docNo=<%=documentNo %>">상신</button>
</form>
</body>
</html>