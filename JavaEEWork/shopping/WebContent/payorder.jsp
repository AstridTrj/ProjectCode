<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>订单支付验证</title>
	</head>
	<body>
		<div align="center">
		<fieldset style="width: 800px; height: 400px; margin: 50px;">
			<legend style="font-size: 5;">账单支付</legend><br><br><br><br>
			<form action="payOrder.order" method="post">
				<table border="1" cellpadding="15" cellspacing="0">
					<tr>
						<td colspan="4" align="center">
							共<big style="color: red;">${sessionScope.usercart[sessionScope.curuser.username].booknum }</big>件图书商品，
							总价: <big style="color: red;">${sessionScope.usercart[sessionScope.curuser.username].totalMoney }</big>元
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">支付密码</td>
						<td colspan="2" align="center"> <input type="password" name="paypwd" style="height: 30px;"> </td>
					</tr>
					<tr>
						<td align="center" colspan="4">
							<input type="submit" value="取消" name="pose">
							<input type="submit" value="确定" name="pose">
						</td>
					</tr>
				</table>
				<h4 style="color: red;">${requestScope.payInfo }</h4>
			</form>
		</fieldset>
		</div>
	</body>
</html>