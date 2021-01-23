package reqResp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name="CheckInclude", urlPatterns="/CheckInclude")
public class CheckInclude extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//此处设置了各种编码，但是在包含页面仍然不能显示
		resp.setContentType("text/html;charset=utf-8");
		//resp.setCharacterEncoding("utf-8");
		//req.setCharacterEncoding("utf-8");
		
		PrintWriter out = resp.getWriter();
		out.println("此处中文显示<br>");
		out.println("URL: " + req.getRequestURL() + "<br>");
		out.println("URL param is " + req.getParameter("param") + "<br>");
		//此处不能关闭，由于在前一个页面中，include之后还有执行语句，因此若此处关闭
		//则前一页面不能继续向浏览器页面输出内容
		//out.close();
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
