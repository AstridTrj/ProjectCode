<%@page import="httpSession.Shoppingcart"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>DeleteOne</title>
	</head>
	<body>
		<%
			//获取需要删除的书名
			String deName = request.getParameter("book");
			Shoppingcart cart = (Shoppingcart)session.getAttribute("cart");
			if(cart != null){
				cart.delItem(deName);
				//删除之后重定向到显示页面
				response.sendRedirect("showCart.jsp");
			}
		%>
	</body>
</html>