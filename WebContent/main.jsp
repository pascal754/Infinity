<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="net.infinity.db.*" %>


<% session.setMaxInactiveInterval(-1);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<style>

ul{list-style:none;}
ul li ul li:hover{background-color:#D4F4FA;}
a { text-decoration:none; color:#000000;}

#nav_menu1 {
list-style-type:none;
padding-left:0px;
position:relative;
top:54px;
left:15%;

}

#nav_menu2{
list-style-type:none;
padding-left:0px;
position:fixed;
top:5px;
right:10%;

}

#nav_menu3{
list-style-type:none;
padding-left:0px;
position:fixed;
top:20px;
left:1500px;

}

#nav_menu ul li {
display:inline;
border-left: 1px solid #c0c0c0;
padding: 0px 10px 0px 10px;
margin: 5px 0px 5px 0px;
}

#nav_menu ul li:first-child {border-left: none;}

#sidebar {
		
			
		float:left; width:180px; background-color:#DEDEDE; padding-left:-20px;}

#content {float:left; width:80%;}

.on{text-decoration: underline; font-weight:bold;}

header{
	text-align:center;
	font-size:25px;
	
	
}




</style>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js" type="text/javascript"></script>




</head>

<header>
	<h1>Document Approval System</h1>
</header>

<body>
	
	
		<%
			String name = (String)session.getAttribute("name");
			String id = (String)session.getAttribute("id");
			int title_code = (int)session.getAttribute("title_code");
			EmpDAO empDao = new EmpDAO();
			String teamName = empDao.getTeamName(Integer.parseInt(id));
			String title = empDao.getTitle(Integer.parseInt(id));
			empDao.dbClose();
		%>
			Welcome
			&nbsp;
			<%=teamName %>
			&nbsp;	
			<%=name %>
			&nbsp;
			<%=title %>
		
			<%if (name.equals("momo")) {
				response.sendRedirect("adminPage.jsp");
			}%>
	
<br> 

<div id="nav_menu">

<ul id="nav_menu1">
	<%if(title_code==1){ %>
	<li><a href="approvalLine.jsp" target="content"><b>결재문서 작성</b></a></li><br><br>
	<%} %>
</ul>	

<ul id="nav_menu2">
	<a href="logoutProcess.jsp"><strong><h3>Logout</h3></strong></a>
</ul>
</div>

<br><br>

<div id="sidebar">

<ul>
		<%
	if (title_code==2) {
%>
	<li><a><b>팀장 문서함</b></a>
		<ul>
			<li class="list"><a href="documentPendingReceiving.do" target="content">수신대기</a></li>
			<li class="list"><a href="documentBeingReceived.do" target="content">수신진행</a></li>
			<li class="list"><a href="documentCompleteReceiving.do" target="content">수신완료</a></li>
			<li class="list"><a href="documentPendingSendingToTeamLeader.do" target="content">발신대기</a></li>
			<li class="list"><a href="documentPendingSendingToCEOByTeam.do" target="content">발신진행</a></li>
			<li class="list"><a href="documentCompleteByTeamLeader.do" target="content">발신완료</a></li>
			<li class="list"><a href="documentRejected.do" target="content">반려함</a></li>
			<li class="list"><a href="documentReturnedPending.do" target="content">반송대기</a></li>
			<li class="list"><a href="documentReturnedConfirmed.do" target="content">반송완료</a></li>
			<li class="list"><a href="documentReturnedReceived.do" target="content">반송수신</a></li>
		</ul>
		</li>
	<%}else if(title_code==3){ %>
	<li><a><b>사장 문서함</b></a>
		<ul>
			<li class="list"><a href="documentPendingSendingToCEO.do" target="content">대기문서</a></li>
			<li class="list"><a href="documentCompleteByCEO.do" target="content">완료문서</a></li>
			<li class="list"><a href="documentRejectedByCEO.do" target="content">반려함</a></li>
			<li class="list"><a href="documentReturnedReceivedToCEO.do" target="content">반송수신</a></li>
		</ul>
		</li>
		
<%	
	}else{
%> 

	<li><a><b>개인 문서함</b></a>
		<ul>
			<li class="list"><a href="draft.jsp" target="content">작성중 문서</a></li>
			<li class="list"><a href="documentBeingSent.jsp" target="content">발신진행</a></li>
			<li class="list"><a href="documentCompleteByTeamMember.do" target="content">발신완료</a></li>
			<li class="list"><a href="documentPendingReceivingToTeamMember.do" target="content">수신대기</a></li>
			<li class="list"><a href="documentCompleteReceivingByTeamMember.do" target="content">수신완료</a></li>
		</ul>
		</li>
		<% }%>
<%if(title_code==1){ %>
	<li><a><b>팀 수신함</b></a>
		<ul>
			<li class="list"><a href="documentPendingReceiving.do" target="content">수신대기</a></li>
			<li class="list"><a href="documentBeingReceived.do" target="content">수신진행</a></li>
			<li class="list"><a href="documentCompleteReceiving.do" target="content">수신완료</a></li>
		</ul>
		</li>
	<li><a><b>팀 발신함</b></a>
		<ul>
			<li class="list"><a href="documentBeingSentByTeam.do" target="content">발신진행</a></li>
			<li class="list"><a href="documentCompleteByTeamLeader.do" target="content">발신완료</a></li>
		</ul>
		</li>
	<li><a><b>반려함</b></a>
		<ul>
			<li class="list"><a href="documentRejected.do" target="content">반려함</a></li>
		</ul>
		</li>
	<li><a><b>반송함</b></a>
		<ul>
			<li class="list"><a href="documentReturnedPending.do" target="content">반송진행</a></li>
			<li class="list"><a href="documentReturnedConfirmed.do" target="content">반송완료</a></li>
		</ul>
		</li>
	<li><a><b>반송수신함</b></a>
		<ul>
			<li class="list"><a href="documentReturnedReceived.do" target="content">반송수신</a></li>
		</ul>
		</li>
		<%} %>
</ul>
<script type="text/javascript">

$('.list').click(function() {
	 $('.list').removeClass('on');
	 $(this).addClass('on');
	});

</script>
</div>

<div id="content">
<iframe name="content" width="100%" height="600px" ></iframe>
</div>
</body>
</html>