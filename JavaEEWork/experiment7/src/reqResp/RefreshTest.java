package reqResp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name="RefreshTest", urlPatterns="/RefreshTest")
public class RefreshTest extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//设置页面一秒刷新一次	
		resp.setHeader("Refresh", "1");
		//可添加指定时间刷新后跳转的url
		//resp.setHeader("Refresh", "5;url=https://www.baidu.com");
		PrintWriter out = resp.getWriter();
		out.print(new java.util.Date());
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
