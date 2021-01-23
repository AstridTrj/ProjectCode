package filterTest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name="LoginCheck", urlPatterns="/LoginCheck")
public class LoginCheck extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//����Ȩ�޵��û�����������֤
		HttpSession session = req.getSession();
		String user = (String)session.getAttribute("user");
		String pwd = req.getParameter("pwd");
		//����user��session��
		if(user == null){
			session.setAttribute("user", req.getParameter("user"));
		}
		//����user�Ѿ�ͨ��Ȩ����֤���˴�ֻ��������֤
		if(pwd.equals("12345")){
			//������֤�ɹ�
			req.getRequestDispatcher("/main.jsp").forward(req, resp);
		}
		else{
			//�����������
			req.getRequestDispatcher("/form2.jsp").forward(req, resp);
		}
	}
}
