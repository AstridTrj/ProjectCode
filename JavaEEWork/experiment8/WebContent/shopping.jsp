<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Shopping</title>
	</head>
	<body>
		<form action="ServletShopping" method="post">
			<h2>选购需要的图书</h2>
			<hr>
				<input type="checkbox" name="item" value="0">《大地之灯》  ￥58.00<br>
				<input type="checkbox" name="item" value="1">《我与地坛》  ￥39.00<br>
				<input type="checkbox" name="item" value="2">《沉睡的人鱼之家》  ￥64.00<br>
				<input type="checkbox" name="item" value="3">《有问集》  ￥28.00<br>
				<input type="checkbox" name="item" value="4">《灯下尘》  ￥26.00<br>
				<input type="checkbox" name="item" value="5">《扶轮问路》  ￥29.00<br>
			<hr>
			<input type="submit" name="submit" value="加入购物车">
		</form>
		<a href="showCart.jsp">查看购物车</a>
	</body>
</html>