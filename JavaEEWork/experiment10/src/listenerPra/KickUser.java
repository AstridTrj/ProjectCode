package listenerPra;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name="KickUser", urlPatterns="/KickUser")
public class KickUser extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//��ȫ�����л�ȡ�û�-sessionӳ���map
		ServletContext context = req.getServletContext();
		Map<String, HttpSession> map = (Map<String, HttpSession>)context.getAttribute("map");
		//��ȡ��Ҫ�߳����û���
		String name = req.getParameter("name");
		HttpSession session = map.get(name);
		//����Ӧ��sessionע����ɾ����Ӧ���û���
		session.invalidate();
		map.remove(name);
		req.getRequestDispatcher("/allusers.jsp").forward(req, resp);
	}
}
