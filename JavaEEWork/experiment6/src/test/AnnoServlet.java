package test;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="AnnoServlet", urlPatterns="/AnnoServlet", 
						initParams={
								@WebInitParam(name="注解1", value="第一个注解参数"),
								@WebInitParam(name="注解2", value="第二个注解参数")
						})
public class AnnoServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Enumeration<String> em = this.getInitParameterNames();
		System.out.println("注解方式参数获取...");
		while(em.hasMoreElements()){
			String name = (String) em.nextElement();
			String value = (String) this.getInitParameter(name);
			System.out.println(name + " = " + value);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
