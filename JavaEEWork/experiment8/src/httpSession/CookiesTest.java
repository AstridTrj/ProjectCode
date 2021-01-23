package httpSession;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name="CookiesTest", urlPatterns="/CookiesTest")
public class CookiesTest extends HttpServlet {
	
	int number = 100;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		//Get cookies
		Cookie[] cookies = req.getCookies();
		if(cookies != null){
			//print the cookie
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				String value = cookie.getValue();
				int maxAge = cookie.getMaxAge();
				out.println("Cookie's name: " + name);
				out.println("Cookie's value: " + value);
				out.println("Cookie's maxAge: " + maxAge);
			}
		}
		else{
			out.println("No cookies there. ");
		}
		//Create a new cookie and add it
		Cookie neCoo = new Cookie("cookie-" + number, "value-" + number);
		neCoo.setMaxAge(-1);
		resp.addCookie(neCoo);
		number++;
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
