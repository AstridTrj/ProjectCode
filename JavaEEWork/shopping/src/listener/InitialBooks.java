package listener;

import java.util.HashMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import control.BookCart;
import dao.BookDao;
import vo.OneBook;
import vo.Page;

@WebListener
public class InitialBooks implements ServletContextListener, HttpSessionListener, ServletRequestListener{

	@Override
	public void contextDestroyed(ServletContextEvent scEvent) {
		//当context被销毁时，清除图书信息
		scEvent.getServletContext().removeAttribute("books");
	}

	@Override
	public void contextInitialized(ServletContextEvent scEvent) {
		//当一个context创建时，初始化第一页的图书信息，将数据库中的图书信息读取出来用于jsp显示
		BookDao bookDao = new BookDao();
		HashMap<String, OneBook> books = bookDao.getAllBooks();
		
		scEvent.getServletContext().setAttribute("allbook", books);
	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		//当session创建时，初始化一个用户名-购物车的map
		HttpSession session = event.getSession();
		HashMap<String, BookCart> usercart = new HashMap<String, BookCart>();
		session.setAttribute("usercart", usercart);
		System.out.println("cart create");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		
	}

	@Override
	public void requestDestroyed(ServletRequestEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestInitialized(ServletRequestEvent scEvent) {
	}

}
