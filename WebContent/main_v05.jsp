<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<style>

ul{list-style:none;}
ul li ul li:hover{background-color:#D4F4FA;}
a { text-decoration:none; color:#000000;}

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

#sidebar {float:left; margin-right:30px;}

#content {float:left; width:1004px; height:604px; border:1px solid black;}

.on{font-weight:900;}
.on:before{content:">";}

</style>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js" type="text/javascript"></script>




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
	<%if(title_code==1){ %>
	<li><a href="approvalLine.jsp" target="content">결재문서 작성</a></li>
	<%} %>
	<li><a href="logoutProcess.jsp">Logout</a></li>
</ul>
</div>

<br><br>

<div id="sidebar">

<ul>
		<%
	if (title_code==2) {
%>
	<li><a>팀장 문서함</a>
		<ul>
			<li class="list"><a href="documentPendingReceiving.do" target="content">수신대기</a></li>
			<li class="list"><a href="1-1" target="content">수신진행</a></li>
			<li class="list"><a href="1-2" target="content">수신완료</a></li>
			<li class="list"><a href="pendingSending.do" target="content">발신대기</a></li>
			<li class="list"><a href="pendingSendingToCEO.do" target="content">발신진행</a></li>
			<li class="list"><a href="documentCompleteByTeamLeader.do" target="content">발신완료</a></li>
			
		</ul>
		</li>
	<%}else if(title_code==3){ %>
	<li><a>사장 문서함</a>
		<ul>
			<li class="list"><a href="pendingSendingToCEO.do" target="content">대기문서</a></li>
			<li class="list"><a href="documentCompleteInCEO.do" target="content">완료문서</a></li>
		</ul>
		</li>
		
<%	
	}else{
%> 

	<li><a>개인 문서함</a>
		<ul>
			<li class="list"><a href="draft.jsp" target="content">작성중 문서</a></li>
			<li class="list"><a href="documentBeingSent.jsp" target="content">발신진행</a></li>
			<li class="list"><a href="documentCompleteInTeamMember.do" target="content">발신완료</a></li>
			<li class="list"><a href="1-1" target="content">수신진행</a></li>
			<li class="list"><a href="1-2" target="content">수신완료</a></li>
		</ul>
		</li>
		<% }%>
<%if(title_code!=3){ %>
	<li><a>팀 수신</a>
		<ul>
			<li class="list"><a href="documentPendingReceiving.do" target="content">수신대기</a></li>
			<li class="list"><a href="3-2" target="content">수신진행</a></li>
			<li class="list"><a href="3-3" target="content">수신완료</a></li>
		</ul>
		</li>
	<li><a>팀 발신</a>
		<ul>
			<li class="list"><a href="pendingSendingToCEO.do" target="content">발신진행</a></li>
			<li class="list"><a href="documentCompleteByTeamLeader.do" target="content">발신완료</a></li>
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
<iframe name="content" width="1000px" height="600px" ></iframe>
</div>
</body>
</html>