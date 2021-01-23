<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>订单查询</title>
	</head>
	<body>
		<div align="right">
			<a href="welcome.jsp">首页</a>&nbsp;|&nbsp;
			欢迎您，${sessionScope.curuser.username }&nbsp;|&nbsp;
			
			<c:if test="${sessionScope.curuser.username!='root' }">
				<a href="showcart.jsp">我的购物车</a>&nbsp;|&nbsp;
			</c:if>
			<c:if test="${sessionScope.curuser.username=='root' }">
				<a href="page.book">返回</a>&nbsp;|&nbsp;
			</c:if>
			<a href="logout.user">注销</a>&nbsp;|&nbsp;
		</div>
		
		<h2 align="center">用户订单信息</h2>
		<table align="center" border="1" cellpadding="10" cellspacing="0">
			<tr>
				<th>订单编号</th>
				<th>用户名</th>
				<th>图书名称</th>
				<th>图书价格</th>
				<th>购买数量</th>
				<th>付款总价</th>
				<th>付款时间</th>
				<th>用户地址</th>
			</tr>
			<c:if test="${requestScope.order != null }">
				<c:forEach var="oneorder" items="${requestScope.order }">
					<tr>
						<td align="center">${oneorder.key }</td>
						<td align="center">${oneorder.value.username }</td>
						<td align="center">《${oneorder.value.bookname }》</td>
						<td align="center">￥${oneorder.value.bookprice }</td>
						<td align="center">${oneorder.value.buycount }</td>
						<td align="center">${oneorder.value.totalMoney }</td>
						<td align="center">${oneorder.value.buydate }</td>
						<td align="center">${oneorder.value.address }</td>
					</tr>
				</c:forEach>
			</c:if>
			<c:if test="${empty requestScope.order }">
				<tr>
					<td colspan="8" align="center">您暂无订单</td>
				</tr>
			</c:if>
		</table>
	</body>
</html>