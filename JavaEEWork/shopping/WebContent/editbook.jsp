<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>编辑图书信息</title>
	</head>
	<body>
		<div align="center">
		<fieldset style="width: 800px; height: 400px; margin: 50px;">
			<legend style="font-size: 5;">图书信息编辑</legend><br><br>
			<form action="update.book?no=${param.no }" method="post">
				<table border="1" cellpadding="15" cellspacing="0">
					<tr>
						<td align="center">图书编号</td>
						<td align="center"> <input type="text" name="bookno" value="${param.no }"> </td>
					</tr>
					<tr>
						<td align="center">图书名称</td>
						<td align="center"> <input type="text" name="bookname" value="${applicationScope.allbook[param.no].bookname }"> </td>
					</tr>
					<tr>
						<td align="center">图书价格</td>
						<td align="center"> <input type="text" name="bookprice" value="${applicationScope.allbook[param.no].bookprice }"> </td>
					</tr>
					<tr>
						<td align="center">图书库存量</td>
						<td align="center"> <input type="text" name="bookcount" value="${applicationScope.allbook[param.no].bookcount }"> </td>
					</tr>
					<tr>
						<td align="center" colspan="2">
							<input type="submit" value="取消" name="pose">
							<input type="submit" value="确定" name="pose">
						</td>
					</tr>
				</table>
			</form>
		</fieldset>
		</div>
	</body>
</html>