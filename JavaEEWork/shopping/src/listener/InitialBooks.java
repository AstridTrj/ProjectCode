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
		//��context������ʱ�����ͼ����Ϣ
		scEvent.getServletContext().removeAttribute("books");
	}

	@Override
	public void contextInitialized(ServletContextEvent scEvent) {
		//��һ��context����ʱ����ʼ����һҳ��ͼ����Ϣ�������ݿ��е�ͼ����Ϣ��ȡ��������jsp��ʾ
		BookDao bookDao = new BookDao();
		HashMap<String, OneBook> books = bookDao.getAllBooks();
		
		scEvent.getServletContext().setAttribute("allbook", books);
	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		//��session����ʱ����ʼ��һ���û���-���ﳵ��map
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
