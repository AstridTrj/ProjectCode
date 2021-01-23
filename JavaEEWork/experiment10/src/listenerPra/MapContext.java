package listenerPra;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;

@WebListener
public class MapContext implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sc) {
		//创建一个map，为用户名到其session的映射
		Map<String, HttpSession> map = new HashMap<String, HttpSession>();
		//将map设置到全局域
		ServletContext context = sc.getServletContext();
		context.setAttribute("map", map);
	}

}
