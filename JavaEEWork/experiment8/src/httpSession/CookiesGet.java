package httpSession;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name="CookiesGet", urlPatterns="/CookiesGet")
public class CookiesGet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Cookie[] cookies = req.getCookies();
		HttpSession session = req.getSession();
		if(cookies != null){
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("sessionId")){
					System.out.println("sessionId: " + cookie.getValue());
					break;
				}
			}
		}
		Shoppingcart cart = (Shoppingcart)session.getAttribute("cart");
		Map<String, Integer> items = cart.getItems();
		//map����: ����keySet�������ؼ�key��Set����
		for(String key: items.keySet()){
			System.out.println(key + ": " + items.get(key) + "��.");
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
