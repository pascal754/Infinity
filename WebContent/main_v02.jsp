<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<style>

ul{list-style:none;}
ul li:hover{background-color:#D4F4FA;}
a { text-decoration:none ; color:#000000}

#nav_menu ul {
list-style-type:none;
padding-left:0px;
text-align:center;
}

#nav_menu ul li {
display:inline;
border-left: 1px solid #c0c0c0;
padding: 0px 10px 0px 10px;
margin: 5px 0px 5px 0px;
}

#nav_menu ul li:first-child {border-left: none;}

#sidebar {float:left;}

#content {float:left; width:1004px; height:604px; border:1px solid black;}

#sidebar ul ul{display:none;}

</style>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js" type="text/javascript"></script>

<script>
$(function(){ $("#sidebar a").click(function(){ 
	$("#sidebar ul ul").slideUp(); 
		if(!$(this).next().is(":visible")) {
				$(this).next().slideDown();
				} 
		}) 
	})

</script>

</head>
<body>
	<h1>Main Page</h1>
<%
	String name = (String)session.getAttribute("name");
	String id = (String)session.getAttribute("id");
	int title_code = (int)session.getAttribute("title_code");
%>
	Welcome
	
	<%=name %>
	<%=title_code %>
	<%
	if (name.equals("momo")) {
%>
		<br>관리자님 반갑습니다.<br>
		<a href="memberList.jsp">회원 목록 보기</a>
<%	
	}
%>

<br><br>

<div id="nav_menu">
<ul>
	<li><a href="approvalLine.jsp">결재문서 작성</a></li>
	<li><a href="logoutProcess.jsp">Logout</a></li>
</ul>
</div>

<br><br>

<div id="sidebar">
<ul>
		<%
	if (title_code==2) {
%>
	<li><a>팀장 문서함</a>&nbsp&nbsp
		<ul>
			<li><a href="1-1" target="content">수신대기</a>&nbsp&nbsp</li>
			<li><a href="1-1" target="content">수신진행</a>&nbsp&nbsp</li>
			<li><a href="1-2" target="content">수신완료</a></li>
			<li><a href="pendingSending.do" target="content">발신대기</a></li>
			<li><a href="pendingSendingToCEO.do" target="content">발신진행</a></li>
			<li><a href="2-3" target="content">발신완료</a></li>
			
		</ul>
		</li>
<%	
	}else{
%> 

	<li><a>개인 문서함</a>&nbsp&nbsp
		<ul>
			<li><a href="draft.jsp" target="content">작성중 문서</a></li>
			<li><a href="documentBeingSent.jsp" target="content">발신진행</a></li>
			<li><a href="2-3" target="content">발신완료</a></li>
			<li><a href="1-1" target="content">수신진행</a>&nbsp&nbsp</li>
			<li><a href="1-2" target="content">수신완료</a></li>
		</ul>
		</li>
		<% }%>

	<li><a>팀 수신</a>&nbsp&nbsp
		<ul>
			<li><a href="3-1" target="content">수신대기</a>&nbsp&nbsp</li>
			<li><a href="3-2" target="content">수신진행</a></li>
			<li><a href="3-3" target="content">수신완료</a></li>
		</ul>
		</li>
	<li><a>팀 발신</a>&nbsp&nbsp
		<ul>
			<li><a href="4-1" target="content">발신진행</a>&nbsp&nbsp</li>
			<li><a href="4-2" target="content">발신완료</a></li>
		
		</ul>
		</li>
</ul>
</div>

<div id="content">
<iframe name="content" width="1000px" height="600px" ></iframe>
</div>
</body>
</html>