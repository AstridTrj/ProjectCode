package control;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDao;
import vo.Users;

@WebServlet(name="LoginDeal", urlPatterns="*.user")
public class LoginDeal extends HttpServlet {
	/**
	 * ���÷�������û������Ĵ���������¼��֤�Լ��û�ע��
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//��ȡ��Ӧ�ķ�����
		String path = req.getServletPath();
		String methodName = path.substring(1, path.length()-5);
		//���÷�����ö�Ӧ�ķ���
		try {
			Method method = getClass().getDeclaredMethod(methodName, HttpServletRequest.class,
					HttpServletResponse.class);
			method.invoke(this, req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
	/**
	 * �û���¼��֤
	 * @param req
	 * @param resp
	 */
	public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		//�û���¼��֤
		//1.����Ƿ���ڸ��û�������������ת����¼ʧ��ҳ�棬��������2���ж�
		//2.����û��������֤�룬���д�������ת����¼ʧ��ҳ�棬�޴��������ͼ�鹺��ϵͳ
		String username = req.getParameter("name");
		String pwd = req.getParameter("pwd");
		String inputcode = req.getParameter("code");
		String validcode = (String) req.getSession().getAttribute("validcode");
		UserDao userDao = new UserDao();
		Users users = userDao.check(username);
		if (users.getUsername() == null) {
			req.setAttribute("loginstatue", "�����ڸ��û�!");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}
		else {
			if (users.getUserpwd().equals(pwd) && inputcode.equals(validcode)) {
				req.getSession().setAttribute("curuser", users);
				req.getRequestDispatcher("/page.book").forward(req, resp);
			}
			else {
				req.setAttribute("loginstatue", "�û��������֤���������!");
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
			}
		}
	}
	/**
	 * �û�ע�����
	 * @param req
	 * @param resp
	 */
	public void register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String name = req.getParameter("rname");
		String pwd = req.getParameter("rpwd");
		String pwd2 = req.getParameter("rpwd2");
		String addr = req.getParameter("radd");
		String paypwd = req.getParameter("rpay");
		String code = req.getParameter("rcode");
		String validcode = (String) req.getSession().getAttribute("validcode");
		UserDao userDao = new UserDao();
		Users users = userDao.check(name);
		if (users.getUsername() != null) {
			req.setAttribute("registerstatue", "�Ѵ��ڸ��û�������������!");
			req.getRequestDispatcher("/register.jsp").forward(req, resp);
		}
		else {
			if(!pwd.equals(pwd2)){
				req.setAttribute("registerstatue", "�������벻һ�£�����������!!");
				req.getRequestDispatcher("/register.jsp").forward(req, resp);
			}
			else {
				if(!code.equals(validcode)){
					req.setAttribute("registerstatue", "��֤�������������������!!");
					req.getRequestDispatcher("/register.jsp").forward(req, resp);
				}
				else {
					//д�����ݿ�
					userDao.userWrite(name, pwd, addr, paypwd);
					req.getRequestDispatcher("/successRegister.jsp").forward(req, resp);
				}
			}
		}
	}
	//�û�ע��
	public void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		req.getSession().removeAttribute("curuser");
		req.getRequestDispatcher("welcome.jsp").forward(req, resp);;
	}
}




