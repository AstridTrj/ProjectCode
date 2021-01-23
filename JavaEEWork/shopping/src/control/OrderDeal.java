package control;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.OrderDao;
import vo.Order;
import vo.Users;


@WebServlet(name="OrderDeal", urlPatterns="*.order")
public class OrderDeal extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//获取对应的方法名
		String path = req.getServletPath();
		String methodName = path.substring(1, path.length()-6);
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
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
	//获取订单
	protected void getOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Users users = (Users) req.getSession().getAttribute("curuser");
		HashMap<String, Order> allorder;
		OrderDao orderDao = new OrderDao();
		
		//若为管理员则获取所有用户的订单
		if(users.getUsername().equals("root")){
			allorder = orderDao.getAllOrder();
		}
		else{
			allorder = orderDao.getOrderByUsername(users.getUsername());
		}
		req.setAttribute("order", allorder);
		req.getRequestDispatcher("/order.jsp").forward(req, resp);
	}
	//付款确认并生成订单
	protected boolean payOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pose = request.getParameter("pose");
		String pwd = request.getParameter("paypwd");
		HttpSession session = request.getSession();
		Users users = (Users) session.getAttribute("curuser");
		
		//判断：如果是以取消按钮提交则返回订单页面
		if(pose.equals("取消")){
			request.getRequestDispatcher("/showcart.jsp").forward(request, response);
			return false;
		}
		else{
			if(!users.getPaypwd().equals(pwd)){
				request.setAttribute("payInfo", "支付密码错误，请重新输入");
				request.getRequestDispatcher("/payorder.jsp").forward(request, response);
				return false;
			}
		}
		HashMap<String, BookCart> usercart = (HashMap<String, BookCart>) session.getAttribute("usercart");
		BookCart bookCart = usercart.get(users.getUsername());
		OrderDao orderDao = new OrderDao();
		//获取时间
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String buytime = formatter.format(currentTime);
		
		for (String bookno : bookCart.getBookInfo().keySet()) {
			//生成订单
			Order order = new Order();
			int lens = orderDao.getTotalCount() + 10000;
			order.setOrderno(String.valueOf(lens));
			order.setUsername(users.getUsername());
			order.setBookname(bookCart.getBookInfo().get(bookno).getBookname());
			order.setBookprice(bookCart.getBookInfo().get(bookno).getBookprice());
			order.setBuycount(String.valueOf(bookCart.getBookCnt().get(bookno)));
			double price = bookCart.getBookCnt().get(bookno) * Double.valueOf(bookCart.getBookInfo().get(bookno).getBookprice());
			order.setTotalMoney(String.valueOf(price));
			order.setBuydate(buytime);
			order.setAddress(users.getAddress());
			//写入数据库
			orderDao.insertOrder(order);
		}
		usercart.remove(users.getUsername());
		request.getRequestDispatcher("/paysuccess.jsp").forward(request, response);
		
		return true;
	}
}
