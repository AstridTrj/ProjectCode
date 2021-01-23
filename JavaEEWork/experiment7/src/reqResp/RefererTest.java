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
		//��������ϰ��ͨ����ȡ����ͷ�е���Դurl�����ж�
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		//��ȡ����ͷ��referer��Ϣ
		String referer = req.getHeader("referer");
		//��ȡ��Ҫ���ʵ�Ŀ���url�����ڶ�referer�����ж�
		String obj = "http://" + req.getServerName();
		out.print("Ŀ����ַ��ʼ����: " + obj + "<br>");
		out.print("Ŀ����ַ: " + req.getRequestURL() + "<br>");
		//����referer������Ϊ�ջ����ǷǷ�����Դ��Ҫ�������ֹ,�෴���������
		if(referer != null && referer.startsWith(obj)){
			out.print("�Ϸ�����Դ��������ʡ�<br>");
		}
		else{
			out.print("�Ƿ���Դ�����������(�˴����ض����������ҳ��)��");
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
