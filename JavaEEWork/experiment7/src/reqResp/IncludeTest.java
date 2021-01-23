package reqResp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name="IncludeTest", urlPatterns="/IncludeTest")
public class IncludeTest extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//��Ҫ��PrintWriter֮ǰ���ñ��룬�����ڰ���ҳ�治��������ʾ����
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		RequestDispatcher rd = req.getRequestDispatcher("/CheckInclude?param=hello");
		
		out.println("Before source component include<br>");
		rd.include(req, resp);
		out.println("After source component include<br>");
		
		out.close();
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
