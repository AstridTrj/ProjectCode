<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>评论主页</title>
	</head>
	<body>
		<form action="SensitiveWord" method="post">
			用户: <input type="text" name="name"><br><br>
			<fieldset style="width: 200px;">
				<legend>评论区</legend>
				<textarea rows="10" cols="50" name="comment"></textarea>
			</fieldset><br>
			<input type="submit" value="提交">
		</form>
	</body>
</html>