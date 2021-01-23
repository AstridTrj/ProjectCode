package listenerPra;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class UserListener implements ServletContextListener, HttpSessionListener {
	private AtomicInteger user = null;
	@Override
	public void sessionCreated(HttpSessionEvent sca) {
		//session����ʱ�û�����1
		this.user.incrementAndGet();
		sca.getSession().getServletContext().setAttribute("user", this.user);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sca) {
		//session����ʱ�û�����1
		this.user.decrementAndGet();
		sca.getSession().getServletContext().setAttribute("user", this.user);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sca) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent sca) {
		//�½�ȫ�ֱ���user������ͳ�������û���
		this.user = new AtomicInteger(1);
		sca.getServletContext().setAttribute("user", this.user);
	}

}
