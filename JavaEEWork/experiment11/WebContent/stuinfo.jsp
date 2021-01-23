<%@page import="stuPrint.StuBean"%>
<%@page import="java.util.HashMap"%>
<%@page import="stuPrint.StuDao"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>学生信息展示</title>
	</head>
	<body>
		<%
			StuDao stu = new StuDao();
			HashMap<String, StuBean> stuinfo = stu.getAllInfo();
		%>
		<table border="1" cellpadding="5" cellspacing="0">
			<caption>学生信息展示</caption>
			<tr>
				<th>姓名</th><th>学号</th><th>性别</th>
			</tr>
			<%
				for(String key: stuinfo.keySet()){
			%>
				<tr>
					<td><%=stuinfo.get(key).getName() %></td>
					<td><%=stuinfo.get(key).getNo() %></td>
					<td><%=stuinfo.get(key).getSex() %></td>
				</tr>
			<%
				}
			%>
		</table>
	</body>
</html>