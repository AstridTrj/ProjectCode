<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>添加图书</title>
	</head>
	<body>
		<div align="center">
		<fieldset style="width: 800px; height: 400px; margin: 50px;">
			<legend style="font-size: 5;">加入购物车</legend><br><br><br><br>
			<form action="addbook.book?no=${param.no }" method="post">
				<table align="center" border="1" cellpadding="8" cellspacing="0">
					<tr>
						<td align="center">图书名</td>
						<td align="center" width="200px">《${applicationScope.allbook[param.no].bookname }》</td>
					</tr>
					<tr>
						<td align="center">图书编号</td>
						<td align="center">${param.no }</td>
					</tr>
					<tr>
						<td align="center">图书价格</td>
						<td align="center">￥${applicationScope.allbook[param.no].bookprice }</td>
					</tr>
					<tr>
						<td align="center">购买数量</td>
						<td align="center" ><input type="text" name="number" style="width: 80px"></td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input type="submit" value="取消" name="pose">
							<input type="submit" value="确定" name="pose">
						</td>
					</tr>
				</table>
			</form>
		</fieldset>
		</div>
		<h3 align="center">${requestScope.overCount }</h3>
	</body>
</html>