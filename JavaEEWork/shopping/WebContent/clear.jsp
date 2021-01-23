<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>清空购物车</title>
	</head>
	<body>
		<c:if test="${empty sessionScope.curuser }">
			<div align="right">
				<a href="login.jsp">登录</a>&nbsp;|&nbsp;
				<a href="register.jsp">注册</a>&nbsp;|&nbsp;
			</div>
		</c:if>
		<c:if test="${!empty sessionScope.curuser}">
			<div align="right">
				欢迎您，${sessionScope.curuser.username }&nbsp;|&nbsp;
				<a href="logout.user">注销</a>&nbsp;|&nbsp;
			</div>
		</c:if>
		<div align="center">
			<fieldset style="width: 800px; height: 400px; margin: 50px;">
				<legend>CLEAR</legend>
				<h1 style="margin-top: 130px;">您的购物车清空成功</h1>
				<a href="page.book?pageno=1">继续购物</a>
			</fieldset>
		</div>
	</body>
</html>