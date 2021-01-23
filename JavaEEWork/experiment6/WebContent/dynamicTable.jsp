<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<html>
	<body>
		<%
			String str = request.getParameter("id");
			out.print(str);
		%>
		<table border="1">
			<tr>
				<td>自然编号</td>
				<td>语言名称</td>
				<td>目前排名</td>
			</tr>
			<%
				String[] name = {"Java", "C#", "Python", "C++", "R", "php"};
				String[] rank = {"1", "4", "2", "3", "5", "6"};
				for(int i = 1; i <= name.length; i++){
			%>
			<tr>
				<td><%=i %></td>
				<td><%=name[i-1] %></td>
				<td><%=rank[i-1] %></td>
			</tr>
			<%
				}
			%>
		</table>
	</body>
</html>