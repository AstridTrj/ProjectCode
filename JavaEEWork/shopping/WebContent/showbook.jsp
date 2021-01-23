<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>购物车图书信息</title>
	</head>
	<body>
		<c:if test="${empty sessionScope.curuser }">
			<div align="right">
				<a href="welcome.jsp">首页</a>&nbsp;|&nbsp;
				<a href="login.jsp">登录</a>&nbsp;|&nbsp;
				<a href="register.jsp">注册</a>&nbsp;|&nbsp;
			</div>
		</c:if>
		<c:if test="${!empty sessionScope.curuser}">
			<div align="right">
				<a href="welcome.jsp">首页</a>&nbsp;|&nbsp;
				欢迎您，${sessionScope.curuser.username }&nbsp;|&nbsp;
				
				<c:if test="${sessionScope.curuser.username!='root' }">
					<a href="showcart.jsp">我的购物车</a>&nbsp;|&nbsp;
				</c:if>
				<c:if test="${sessionScope.curuser.username=='root' }">
					<a href="getOrder.order">用户订单</a>&nbsp;|&nbsp;
				</c:if>
				<a href="logout.user">注销</a>&nbsp;|&nbsp;
			</div>
		</c:if>
		
		<h2 align="center">图书信息</h2>
		<c:if test="${sessionScope.curuser.username=='root' }">
			<h4 align="center">(您是管理员，可修改图书信息和查看客户订单)</h4>
		</c:if>
		<table align="center" border="1" cellpadding="10" cellspacing="0">
			<tr>
				<th>图书编号</th>
				<th>图书名称</th>
				<th>图书价格</th>
				<th>图书库存量</th>
				<c:if test="${!empty sessionScope.curuser and sessionScope.curuser.username != 'root'}">
					<th>购买</th>
				</c:if>
				<c:if test="${sessionScope.curuser.username=='root' }">
					<th>修改信息</th>
				</c:if>
			</tr>
			<c:forEach var="onebook" items="${requestScope.pagebook.items }">
					<tr>
						<td align="center">${onebook.value.bookno }</td>
						<td align="center">《${onebook.value.bookname }》</td>
						<td align="center">￥${onebook.value.bookprice }</td>
						
						<c:if test="${onebook.value.bookcount <= 0 }">
							<td align="center">0</td>
							<c:if test="${!empty sessionScope.curuser and sessionScope.curuser.username != 'root'}">
								<td align="center">暂无库存</td>
							</c:if>
						</c:if>
						
						<c:if test="${onebook.value.bookcount > 0 }">
							<td align="center">${onebook.value.bookcount }</td>
							<c:if test="${!empty sessionScope.curuser and sessionScope.curuser.username != 'root'}">
								<td align="center"><a href="addbook.jsp?no=${onebook.value.bookno }">加入购物车</a></td>
							</c:if>
						</c:if>
						
						<c:if test="${sessionScope.curuser.username=='root' }">
							<td align="center"><a href="editbook.jsp?no=${onebook.value.bookno }">点击编辑</a></td>
						</c:if>
					</tr>
			</c:forEach>
		</table><br>
		<div align="center">
			◆
			<a href="page.book?pageno=1">首页</a>&nbsp;
			<c:if test="${pagebook.pageNo > 1 }">
				<a href="page.book?pageno=${pagebook.pageNo-1 }">上一页</a>&nbsp;
				<a href="page.book?pageno=${pagebook.pageNo-1 }">${pagebook.pageNo-1 }</a>&nbsp;
			</c:if>
			
			<a>[${pagebook.pageNo }]</a>&nbsp;
			
			<c:if test="${pagebook.pageNo < pagebook.pageTotal }">
				<a href="page.book?pageno=${pagebook.pageNo+1 }">${pagebook.pageNo+1 }</a>&nbsp;
				<a href="page.book?pageno=${pagebook.pageNo+1 }">下一页</a>&nbsp;
			</c:if>
			<a href="page.book?pageno=${pagebook.pageTotal }">尾页</a>&nbsp;
			<a>共${pagebook.pageTotal }页</a>&nbsp;
			<a>${pagebook.totalCount }条记录</a>&nbsp;◆
		</div>
	</body>
</html>