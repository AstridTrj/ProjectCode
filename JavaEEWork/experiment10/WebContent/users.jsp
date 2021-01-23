<%@page import="java.util.concurrent.atomic.AtomicInteger"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Online users</title>
	</head>
	<body>
		<h3>目前在线人数为
		<% 
			AtomicInteger user = (AtomicInteger)session.getServletContext().getAttribute("user");
			if(user == null)
				out.print("user is null.<br>");
			else
				out.print(user.get() + "<br>");
		%></h3>
		<a href="SessionDestroy">注销用户</a>
	</body>
</html>