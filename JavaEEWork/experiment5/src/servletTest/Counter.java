package servletTest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class counter
 */
@WebServlet("/counter")
public class Counter extends HttpServlet {
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter pw = response.getWriter();
		ServletContext app = this.getServletContext();
		Integer res = (Integer)app.getAttribute("count");
		if(res == null){
			app.setAttribute("count", 1);
			res = (Integer)app.getAttribute("count");
		}
		pw.write("<h1>这是您第" + res + "次登录. </h1>");
		app.setAttribute("count", res+1);
	}

}
