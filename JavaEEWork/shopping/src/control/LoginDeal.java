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
	 * 利用反射进行用户操作的处理，包括登录验证以及用户注册
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//获取对应的方法名
		String path = req.getServletPath();
		String methodName = path.substring(1, path.length()-5);
		//利用反射调用对应的方法
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
	 * 用户登录验证
	 * @param req
	 * @param resp
	 */
	public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		//用户登录验证
		//1.检查是否存在该用户，不存在则跳转到登录失败页面，否则进入第2步判断
		//2.检查用户密码和验证码，若有错误则跳转到登录失败页面，无错误则进入图书购物系统
		String username = req.getParameter("name");
		String pwd = req.getParameter("pwd");
		String inputcode = req.getParameter("code");
		String validcode = (String) req.getSession().getAttribute("validcode");
		UserDao userDao = new UserDao();
		Users users = userDao.check(username);
		if (users.getUsername() == null) {
			req.setAttribute("loginstatue", "不存在该用户!");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}
		else {
			if (users.getUserpwd().equals(pwd) && inputcode.equals(validcode)) {
				req.getSession().setAttribute("curuser", users);
				req.getRequestDispatcher("/page.book").forward(req, resp);
			}
			else {
				req.setAttribute("loginstatue", "用户密码或验证码输入错误!");
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
			}
		}
	}
	/**
	 * 用户注册操作
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
			req.setAttribute("registerstatue", "已存在该用户，请重新输入!");
			req.getRequestDispatcher("/register.jsp").forward(req, resp);
		}
		else {
			if(!pwd.equals(pwd2)){
				req.setAttribute("registerstatue", "密码输入不一致，请重新输入!!");
				req.getRequestDispatcher("/register.jsp").forward(req, resp);
			}
			else {
				if(!code.equals(validcode)){
					req.setAttribute("registerstatue", "验证码输入错误，请重新输入!!");
					req.getRequestDispatcher("/register.jsp").forward(req, resp);
				}
				else {
					//写入数据库
					userDao.userWrite(name, pwd, addr, paypwd);
					req.getRequestDispatcher("/successRegister.jsp").forward(req, resp);
				}
			}
		}
	}
	//用户注销
	public void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		req.getSession().removeAttribute("curuser");
		req.getRequestDispatcher("welcome.jsp").forward(req, resp);;
	}
}




