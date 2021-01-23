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
		//����һ��map��Ϊ�û�������session��ӳ��
		Map<String, HttpSession> map = new HashMap<String, HttpSession>();
		//��map���õ�ȫ����
		ServletContext context = sc.getServletContext();
		context.setAttribute("map", map);
	}

}
