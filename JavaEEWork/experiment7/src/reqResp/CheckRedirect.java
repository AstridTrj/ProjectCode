package reqResp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name="CheckRedirect", urlPatterns="/CheckRedirect")
public class CheckRedirect extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		//��ȡ�����������õĲ���������RedirectTest�����õĲ���
		String param = (String)req.getAttribute("param");
		out.println("Parameters in request domain: param = " + param);
		System.out.println("Parameters in request domain: param = " + param);
		
		//��ȡurl�е��������
		String urlParam = req.getParameter("urlP");
		out.println("Parameters in request url: urlParam = " + urlParam);
		System.out.println("Parameters in request url: urlParam = " + urlParam);
		
		out.close();
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
