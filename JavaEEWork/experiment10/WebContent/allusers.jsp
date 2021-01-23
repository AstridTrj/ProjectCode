<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>登录用户列表</title>
	</head>
	<body>
		<%
			ServletContext context = request.getServletContext();
			Map<String, HttpSession> map = (Map<String, HttpSession>) context.getAttribute("map");
		%>
		<table border="1">
			<caption>登录用户列表</caption>
			<tr>
				<td>用户名</td>  <td>Session</td>  <td>踢除</td>
			</tr>
			<%
				for(String key: map.keySet()){
			%>
				<tr>
					<td><%=key %></td>
					<td><%=map.get(key) %></td>
					<td> <a href="KickUser?name=<%=key %>">踢除</a> </td>
				</tr>
			<%
				}
			%>
		</table>
	</body>
</html>