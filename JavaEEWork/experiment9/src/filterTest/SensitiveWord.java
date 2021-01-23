package filterTest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name="SensitiveWord", urlPatterns="/SensitiveWord")
public class SensitiveWord extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		String comment = req.getParameter("comment");
		resp.getWriter().print("”√ªß£∫" + name + "<br>");
		resp.getWriter().print("∆¿¬€£∫" + comment + "<br>");
	}
}
