package reqResp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name="ForwardTest", urlPatterns="/ForwardTest")
public class ForwardTest extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		PrintWriter out = resp.getWriter();
		//获取地址栏中的param参数
		String param = req.getParameter("param");
		String print = null;
		if(param == null)
			print = "error param...";
		else
			print = "param is " + param;
		//在源组件中利用request设置属性, 用于验证转发和重定向是否能获取到该参数
		req.setAttribute("param", print);
		ServletContext context = this.getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher("/CheckForward?up="+print);
		//转发之前
		out.println("Before source component forward");
		System.out.println("Before source component forward");
		//进行转发
		dispatcher.forward(req, resp);
		//重定向之后是否执行的验证
		out.println("After source component forward");
		System.out.println("After source component forward");
		
		out.close();
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
