<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>무한발전주식회사</h1>
	<form action="loginProcess.do" method="post">
		<fieldset>
			<legend>Login</legend>
			<table border="0">
				<tr>
					<td>ID:</td>
					<td><input type="text" name="id" pattern="^[1-9]\d\d$"></td>
				</tr>
			
				<tr>
					<td>Password:</td>
					<td><input type="password" name="password"></td>
				</tr>
			
				<tr>
					<td colspan="2"><input type="submit" value="Login">
					</td>
				</tr>
			
			</table>
		</fieldset>
	</form>
</body>
</html>