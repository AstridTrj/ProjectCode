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
		//��ȡ��ַ���е�param����
		String param = req.getParameter("param");
		String print = null;
		if(param == null)
			print = "error param...";
		else
			print = "param is " + param;
		//��Դ���������request��������, ������֤ת�����ض����Ƿ��ܻ�ȡ���ò���
		req.setAttribute("param", print);
		ServletContext context = this.getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher("/CheckForward?up="+print);
		//ת��֮ǰ
		out.println("Before source component forward");
		System.out.println("Before source component forward");
		//����ת��
		dispatcher.forward(req, resp);
		//�ض���֮���Ƿ�ִ�е���֤
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
