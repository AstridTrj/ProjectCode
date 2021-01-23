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
		//对有权限的用户进行密码验证
		HttpSession session = req.getSession();
		String user = (String)session.getAttribute("user");
		String pwd = req.getParameter("pwd");
		//保存user到session中
		if(user == null){
			session.setAttribute("user", req.getParameter("user"));
		}
		//由于user已经通过权限验证，此处只对密码验证
		if(pwd.equals("12345")){
			//密码验证成功
			req.getRequestDispatcher("/main.jsp").forward(req, resp);
		}
		else{
			//输入密码错误
			req.getRequestDispatcher("/form2.jsp").forward(req, resp);
		}
	}
}
