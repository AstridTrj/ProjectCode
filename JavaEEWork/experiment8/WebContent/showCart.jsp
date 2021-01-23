<%@page import="httpSession.GoodsInfo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="httpSession.Shoppingcart"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>购物车内容</title>
	</head>
	<body>
		<%
			Map<String, Integer> items = null;
			Shoppingcart cart = (Shoppingcart)session.getAttribute("cart");
			if(cart == null){
				out.print("<h4>您的购物车暂无内容<h4>");
			}
			else{
				items = cart.getItems();
		%>
		<table border="1">
			<caption>购物车商品</caption>
			<tr>
				<td>书名</td>
				<td>价格</td>
				<td>数量</td>
				<td>数量减一</td>
			</tr>
			<%
				double total = cart.getTotalPrice();
				for(String book : items.keySet()){
					double price = cart.getP().get(book);
					int num = items.get(book);
			%>
				<tr>
					<td>《<%=book %>》</td>
					<td>￥<%=price %></td>
					<td><%=num %></td>
					<td><a href="deleteOne.jsp?book=<%=book%>">删除</a></td>
				</tr>
				
			<%
				}
			%>
		</table>
		总价：￥<%=cart.getTotalPrice() %><br>
		<%
			}
		%>
		<a href="shopping.jsp">继续购物</a>
		<a href="clearCart.jsp">清空购物车</a>
	</body>
</html>