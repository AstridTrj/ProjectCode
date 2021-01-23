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
		//��������
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		//��ȡ��context���󣬴Ӷ�ͨ�����������֤������
		ServletContext context = this.getServletContext();
		Integer count = (Integer) context.getAttribute("counter");
		
		//��ʼΪ�գ�������counter����
		if(count == null){
			context.setAttribute("counter", 1);
			count = (Integer) context.getAttribute("counter");
		}
		
		//�����ҳ����ʾ�鿴
		out.print("<h3>��" + count + "�ε�¼</h3>");
		//��¼������1
		context.setAttribute("counter", count+1);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
