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
	//���Ա任�ļ�����
	@Override
	public void attributeAdded(ServletContextAttributeEvent sca) {
		//�������������ʱ�Ĳ���
		String name = sca.getName();
		String value = String.valueOf(sca.getValue());
		System.out.println("ServletContext�½�����" + name + ", ֵΪ: " + value);
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent sca) {
		//ɾ������ʱ�ļ���
		String name = sca.getName();
		System.out.println("ServletContextɾ��������" + name);
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent sca) {
		//�޸�����ʱ�ļ���
		String name = sca.getName();
		String value = String.valueOf(sca.getValue());
		System.out.println("ServletContext�޸�������" + name + ", �޸ĺ��ֵΪ: " + value);
	}
	
	
	//����ΪServletContextListener���ݣ�����ServletContext�Ĵ���������
	@Override
	public void contextDestroyed(ServletContextEvent sca) {
		System.out.println("����ServletContext......");
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sca) {
		System.out.println("����ServletContext......");
	}

}
