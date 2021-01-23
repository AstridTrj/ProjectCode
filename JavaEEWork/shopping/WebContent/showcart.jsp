<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>购物车</title>
	</head>
	<body>

		<div align="right">
			<a href="welcome.jsp">首页</a>&nbsp;|&nbsp;
			欢迎您，${sessionScope.curuser.username }&nbsp;|&nbsp;
			<a href="getOrder.order">我的订单</a>&nbsp;|&nbsp;
			<a href="logout.user">注销</a>&nbsp;|&nbsp;
		</div>

		<h3 align="center">${sessionScope.curuser.username }的购物车商品信息</h3>
		<table align="center" border="1" cellpadding="10" cellspacing="0">
			<tr>
				<th>图书编号</th>
				<th>图书名称</th>
				<th>图书价格</th>
				<th>购买数量</th>
				<th>删除</th>
			</tr>
			<c:forEach var="cartbook" items="${sessionScope.usercart[sessionScope.curuser.username].bookInfo }">
				<tr>
					<td align="center">${cartbook.value.bookno }</td>
					<td align="center">《${cartbook.value.bookname }》</td>
					<td align="center">￥${cartbook.value.bookprice }</td>
					<td align="center">${sessionScope.usercart[sessionScope.curuser.username].bookCnt[cartbook.value.bookno] }</td>
					<td align="center"><a href="delete.book?bookno=${cartbook.value.bookno }">删除</a></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="6" align="center">
					<c:if test="${sessionScope.usercart[sessionScope.curuser.username] != null }">
						共<big style="color: red;">${sessionScope.usercart[sessionScope.curuser.username].booknum }</big>件图书商品，
						总价: <big style="color: red;">${sessionScope.usercart[sessionScope.curuser.username].totalMoney }</big>元
						<a href="payorder.jsp">去付款</a>
					</c:if>
					<c:if test="${sessionScope.usercart[sessionScope.curuser.username] == null }">
						共<big style="color: red;">0</big>件图书商品，
						总价: <big style="color: red;">0</big>元
					</c:if>
				</td>
			</tr>
		</table><br>
		<div align="center">
			|&nbsp;<a href="page.book">继续购物</a>&nbsp;|&nbsp;
			<c:if test="${sessionScope.usercart[sessionScope.curuser.username] != null }">
				<a href="clear.book">清空购物车</a>&nbsp;|
			</c:if>
		</div>
	</body>
</html>