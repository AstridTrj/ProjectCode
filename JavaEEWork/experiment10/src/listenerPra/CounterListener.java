package listenerPra;

import java.io.PrintWriter;
import java.text.MessageFormat;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

//@WebListener
public class CounterListener implements ServletContextListener, ServletContextAttributeListener {
	//属性变换的监听器
	@Override
	public void attributeAdded(ServletContextAttributeEvent sca) {
		//监听到添加属性时的操作
		String name = sca.getName();
		String value = String.valueOf(sca.getValue());
		System.out.println("ServletContext新建属性" + name + ", 值为: " + value);
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent sca) {
		//删除属性时的监听
		String name = sca.getName();
		System.out.println("ServletContext删除了属性" + name);
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent sca) {
		//修改属性时的监听
		String name = sca.getName();
		String value = String.valueOf(sca.getValue());
		System.out.println("ServletContext修改了属性" + name + ", 修改后的值为: " + value);
	}
	
	
	//以下为ServletContextListener内容，监听ServletContext的创建和销毁
	@Override
	public void contextDestroyed(ServletContextEvent sca) {
		System.out.println("销毁ServletContext......");
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sca) {
		System.out.println("创建ServletContext......");
	}

}
