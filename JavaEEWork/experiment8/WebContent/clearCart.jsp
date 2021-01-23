<%@page import="httpSession.Shoppingcart"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Clear Cart</title>
	</head>
		<body>
			<%
				//获取查看是否已经创建购物车，若有则直接删除清空，否则提示购物车无内容
				Shoppingcart cart = (Shoppingcart)session.getAttribute("cart");
				if(cart == null || cart.getNumber() == 0){
					out.print("<h4>您的购物车暂无内容<h4>");
					if(cart != null)
						session.removeAttribute("cart");
				}
				else{
					session.removeAttribute("cart");
					out.print("<h4>清空购物车成功<h4>");
				}
				out.print("<a href='shopping.jsp'>继续购物</a>");
			%>
		</body>
</html>