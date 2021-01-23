package reqResp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="RefererTest", urlPatterns="/RefererTest")
public class RefererTest extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//防盗链练习，通过获取请求头中的来源url进行判断
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		//获取请求头的referer信息
		String referer = req.getHeader("referer");
		//获取需要访问的目标的url，用于对referer进行判断
		String obj = "http://" + req.getServerName();
		out.print("目标网址开始部分: " + obj + "<br>");
		out.print("目标网址: " + req.getRequestURL() + "<br>");
		//处理referer，若其为空或者是非法的来源想要访问则禁止,相反则允许访问
		if(referer != null && referer.startsWith(obj)){
			out.print("合法的来源，允许访问。<br>");
		}
		else{
			out.print("非法来源，不允许访问(此处可重定向到正规访问页面)。");
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
