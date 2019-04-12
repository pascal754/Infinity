<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="joinProcess.jsp" method="post">
	<fieldset>
	<table border=0>
		<tr>
		<td colspan="2" align="center">회원 가입 페이지</td>
		</tr>
		<tr>
			<td>아이디:</td>
			<td><input type="text" name="id" id="id"></td>
		</tr>
		<tr>
			<td>비밀번호:</td>
			<td><input type="password" name="password" id="password"></td>
		</tr>
		<tr>
			<td>이름:</td>
			<td><input type="text" name="name" id="name"></td>
		</tr>
		<tr>
			<td>나이</td>
			<td><input type="text" name="age" id="age"></td>
		</tr>
		<tr>
			<td>성별</td>
			<td><input type="radio" value="male" name="gender" id="gender1">남자&nbsp;
				<input type="radio" value="female" name="gender" id="gender2">여자
			</td>
			
		</tr>
		<tr>
			<td>이메일 주소:</td>
			<td><input type="text" name="email" id="email"></td>
		</tr>
		<tr>
			<td colspan="2" align="center"><input type="submit" value="회원가입">
				&nbsp;&nbsp;<input type="reset" value="다시 작성">
			</td>
		</tr>
	</table>
	</fieldset>
	</form>
</body>
</html>