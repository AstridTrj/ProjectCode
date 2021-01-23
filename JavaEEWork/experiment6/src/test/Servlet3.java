package test;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="Servlet3", urlPatterns="/Servlet3")
public class Servlet3 extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext param = this.getServletContext();
		String value = (String) param.getAttribute("common value");
		System.out.println("获取的共享数据值为：" + value + "\n");
		
		//web应用基础信息和文件信息的查看
		System.out.println("Web应用名称: " + param.getServletContextName());
		System.out.println("Web应用根路径: " + param.getContextPath());
		System.out.println("Servlet AIP主版本: " + param.getMajorVersion());
		System.out.println("Servlet容器的名称和版本: " + param.getServerInfo());
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
