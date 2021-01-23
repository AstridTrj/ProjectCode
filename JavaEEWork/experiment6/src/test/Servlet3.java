package test;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="Servlet3", urlPatterns="/Servlet3")
public class Servlet3 extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext param = this.getServletContext();
		String value = (String) param.getAttribute("common value");
		System.out.println("��ȡ�Ĺ�������ֵΪ��" + value + "\n");
		
		//webӦ�û�����Ϣ���ļ���Ϣ�Ĳ鿴
		System.out.println("WebӦ������: " + param.getServletContextName());
		System.out.println("WebӦ�ø�·��: " + param.getContextPath());
		System.out.println("Servlet AIP���汾: " + param.getMajorVersion());
		System.out.println("Servlet���������ƺͰ汾: " + param.getServerInfo());
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
