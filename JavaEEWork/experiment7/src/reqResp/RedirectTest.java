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
		//��ȡ��ַ���е�param����
		String param = req.getParameter("param");
		String print = null;
		if(param == null)
			print = "error param...";
		else
			print = "param = " + param;
		//��Դ���������request��������, ������֤ת�����ض����Ƿ��ܻ�ȡ���ò���
		req.setAttribute("param", param);
		out.println("Before source component redirection");
		System.out.println("Before source component redirection");
		//ʹ���ض��������ת
		resp.sendRedirect("CheckRedirect?urlP="+param);
		//�ض���֮���Ƿ�ִ�е���֤
		out.println("After source component redirection");
		System.out.println("After source component redirection");
		System.out.println("����Ŀ�����֮ǰ���");
		out.close();
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}
}
