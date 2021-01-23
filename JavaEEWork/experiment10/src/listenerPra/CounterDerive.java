package listenerPra;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="CounterDerive", urlPatterns="/CounterDerive")
public class CounterDerive extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//编码设置
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		//获取到context对象，从而通过对其操作验证监听器
		ServletContext context = this.getServletContext();
		Integer count = (Integer) context.getAttribute("counter");
		
		//初始为空，需增加counter属性
		if(count == null){
			context.setAttribute("counter", 1);
			count = (Integer) context.getAttribute("counter");
		}
		
		//输出到页面显示查看
		out.print("<h3>第" + count + "次登录</h3>");
		//登录次数加1
		context.setAttribute("counter", count+1);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
