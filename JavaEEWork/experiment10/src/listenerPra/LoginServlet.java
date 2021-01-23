package listenerPra;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name="LoginServlet", urlPatterns="/LoginServlet")
public class LoginServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String name = req.getParameter("name");
		String pwd = req.getParameter("pwd");
		//新建user
		User user = new User(name, pwd);
		//将user与session绑定
		HttpSession session = req.getSession();
		session.setAttribute("user", user);
		req.getRequestDispatcher("/welcome.jsp").forward(req, resp);
	}
}
