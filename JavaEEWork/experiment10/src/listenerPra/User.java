package listenerPra;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class User implements HttpSessionBindingListener {
	private String name = null;
	private String pwd = null;
	
	public User(String name, String pwd) {
		this.name = name;
		this.pwd = pwd;
	}
	
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		//��ȫ������ȡ��map
		HttpSession session = event.getSession();
		ServletContext sc = session.getServletContext();
		Map<String, HttpSession> map = (Map<String, HttpSession>) sc.getAttribute("map");
		//�����û���session����map
		map.put(name, session);
		//����ȫ�����map�е��û����
		sc.setAttribute("map", map);
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub

	}

}
