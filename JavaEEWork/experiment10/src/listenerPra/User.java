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
		//从全局域中取出map
		HttpSession session = event.getSession();
		ServletContext sc = session.getServletContext();
		Map<String, HttpSession> map = (Map<String, HttpSession>) sc.getAttribute("map");
		//将本用户与session放入map
		map.put(name, session);
		//更新全局域的map中的用户情况
		sc.setAttribute("map", map);
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub

	}

}
