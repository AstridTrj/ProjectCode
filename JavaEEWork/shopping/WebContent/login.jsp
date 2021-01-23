<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>购物系统登录</title>
	</head>
	<body>
		<script type="text/javascript">
			function refresh() {
				validImg.src = "ValidImg?id=" + Math.random();
			}
		</script>
		<br><br><br><br><br>
		<h1 align="center">Welcome</h1>
		<div align="center">
			<form action="login.user" method="post" name="ValidForm">
				<table frame="void" cellspacing="10">
					<tr>
						<td>用户名</td>
						<td colspan="2"><input type="text" name="name" style="width: 278px; height: 26px"></td>
					</tr>
					<tr>
						<td>密&nbsp;&nbsp;&nbsp;码</td>
						<td colspan="2"><input type="password" name="pwd" style="width: 278px; height: 26px"></td>
					</tr>
					
					<tr>
						<td>验证码</td>
						<td><input type="text" name="code" style="width: 148px; height: 26px"></td>
						<td><img name="validImg" src="ValidImg" onclick="refresh()" style="width: 120px; height: 26px"></td>
					</tr>
					<tr>
						<td> </td>
						<td colspan="2"><input type="submit" value="登录" style="width: 280px; height: 33px"></td>
					</tr>
					<tr>
						<td> </td>
						<td colspan="2" align="right"><a href=register.jsp>没有账号？点击注册</a></td>
					</tr>
				</table>
				<h4 style="color: red;">${requestScope.loginstatue }</h4>
			</form>
		</div>
	</body>
</html>