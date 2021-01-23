package reqResp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name="RedirectTest", urlPatterns="/RedirectTest")
public class RedirectTest extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		//获取地址栏中的param参数
		String param = req.getParameter("param");
		String print = null;
		if(param == null)
			print = "error param...";
		else
			print = "param = " + param;
		//在源组件中利用request设置属性, 用于验证转发和重定向是否能获取到该参数
		req.setAttribute("param", param);
		out.println("Before source component redirection");
		System.out.println("Before source component redirection");
		//使用重定向进行跳转
		resp.sendRedirect("CheckRedirect?urlP="+param);
		//重定向之后是否执行的验证
		out.println("After source component redirection");
		System.out.println("After source component redirection");
		System.out.println("我在目标组件之前输出");
		out.close();
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}
}
