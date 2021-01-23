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
		//�˴������˸��ֱ��룬�����ڰ���ҳ����Ȼ������ʾ
		resp.setContentType("text/html;charset=utf-8");
		//resp.setCharacterEncoding("utf-8");
		//req.setCharacterEncoding("utf-8");
		
		PrintWriter out = resp.getWriter();
		out.println("�˴�������ʾ<br>");
		out.println("URL: " + req.getRequestURL() + "<br>");
		out.println("URL param is " + req.getParameter("param") + "<br>");
		//�˴����ܹرգ�������ǰһ��ҳ���У�include֮����ִ����䣬������˴��ر�
		//��ǰһҳ�治�ܼ����������ҳ���������
		//out.close();
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
