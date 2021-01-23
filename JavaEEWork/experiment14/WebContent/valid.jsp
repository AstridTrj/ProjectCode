<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.awt.Font"%>
<%@page import="java.util.Random"%>
<%@page import="java.awt.Color"%>
<%@page import="java.awt.Graphics"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>验证码生成</title>
	</head>
	<body>
		<%
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("image/jpeg");
			int width = 60, height = 20;
			//创建图像
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			//获取画笔
			Graphics g = image.getGraphics();
			//设置颜色和形状
			g.setColor(new Color(100, 100, 100));
			g.fillRect(0, 0, width, height);
			//产生四位随机数
			Random rnd = new Random();
			int random = rnd.nextInt(8999) + 1000;
			String randStr = String.valueOf(random);
			//将验证码存入session
			session.setAttribute("session", randStr);
			//将验证码显示到图像中
			g.setColor(Color.black);
			g.setFont(new Font("", Font.PLAIN, 20));
			g.drawString(randStr,10, 17);
			for(int i = 0; i < 100; i++){
				int x = rnd.nextInt();
				int y = rnd.nextInt();
				g.drawOval(x, y, 1, 1);
			}
			ImageIO.write(image, "JPEG", response.getOutputStream());
			out.clear();
			out = pageContext.pushBody();
		%>
	</body>
</html>


