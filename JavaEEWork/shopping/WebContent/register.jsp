<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>欢迎注册</title>
	</head>
	<body>
		<script type="text/javascript">
			function refresh() {
				validImg.src = "ValidImg?id=" + Math.random();
			}
		</script>
		<br><br>
		<h1 align="center">填写注册信息</h1>
		<div align="center">
			<form action="register.user" method="post" name="ValidForm">
				<table frame="void" cellspacing="10">
					<tr>
						<td>用户姓名</td>
						<td colspan="2"><input type="text" name="rname" style="width: 278px; height: 26px"></td>
					</tr>
					<tr>
						<td>注册密码</td>
						<td colspan="2"><input type="password" name="rpwd" style="width: 278px; height: 26px"></td>
					</tr>
					<tr>
						<td>确认密码</td>
						<td colspan="2"><input type="password" name="rpwd2" style="width: 278px; height: 26px"></td>
					</tr>
					<tr>
						<td>用户地址</td>
						<td colspan="2"><input type="text" name="radd" style="width: 278px; height: 26px"></td>
					</tr>
					<tr>
						<td>支付密码</td>
						<td colspan="2"><input type="password" name="rpay" style="width: 278px; height: 26px"></td>
					</tr>
					<tr>
						<td>验证码</td>
						<td><input type="text" name="rcode" style="width: 148px; height: 26px"></td>
						<td><img name="validImg" src="ValidImg" onclick="refresh()" style="width: 120px; height: 26px"></td>
					</tr>
					<tr>
						<td> </td>
						<td colspan="2"><input type="submit" value="点击注册" style="width: 280px; height: 33px"></td>
					</tr>
					<tr>
						<td> </td>
						<td colspan="2" align="right"><a href=login.jsp>取消注册，返回登录</a></td>
					</tr>
				</table>
				<h4 style="color: red;">${requestScope.registerstatue }</h4>
			</form>
		</div>
	</body>
</html>