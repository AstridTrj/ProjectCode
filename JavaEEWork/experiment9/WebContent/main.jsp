<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>首页</title>
	</head>
	<body>
		<%
			String name = request.getParameter("user");
			out.println(name + "登录成功<br>");
		%>
		<a href="Logout">注销</a>
	</body>
</html>