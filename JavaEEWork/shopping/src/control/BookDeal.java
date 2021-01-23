package control;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.BookDBUtil;
import dao.BookDao;
import vo.OneBook;
import vo.Page;
import vo.Users;

@WebServlet(name="BookDeal", urlPatterns="*.book")
public class BookDeal extends HttpServlet {
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
		doGet(req, resp);
	}
	
	protected boolean addbook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String pose = request.getParameter("pose");
		//判断：如果是以取消按钮提交则返回购书页面
		if(pose.equals("取消")){
			//如果是以取消按钮提交则进行重定向到初始页面
			this.page(request, response);
			return false;
		}
		
		String bookno = request.getParameter("no");
		int number = Integer.parseInt(request.getParameter("number"));
		HashMap<String, OneBook> books = (HashMap<String, OneBook>) request.getServletContext().getAttribute("allbook");
		OneBook oneBook = books.get(bookno);
		int now = Integer.parseInt(oneBook.getBookcount());
		if(now < number){
			request.setAttribute("overCount", "您购买的数量超限，请重新输入购买数量。");
			request.getRequestDispatcher("/addbook.jsp").forward(request, response);
		}
		else {
			HttpSession session = request.getSession();
			HashMap<String, BookCart> usercart = (HashMap<String, BookCart>) session.getAttribute("usercart");
			Users users = (Users) session.getAttribute("curuser");
			BookCart bookCart = new BookCart();
			if (usercart == null) {
				usercart = new HashMap<String, BookCart>();
			}
			if (usercart.containsKey(users.getUsername())) {
				bookCart = usercart.get(users.getUsername());
			}
			//修改图书库存量
			oneBook.setBookcount(String.valueOf(now-number));
			//将修改后的结果写入数据库
			BookDao bookDao = new BookDao();
			bookDao.updateBookInfo(oneBook);
			//加入用户的购物车并修改session属性
			bookCart.addItem(oneBook, number);
			usercart.put(users.getUsername(), bookCart);
			session.setAttribute("usercart", usercart);
			//跳转以重新加载page数据，否则jsp页面不能获取到数据
			this.page(request, response);
		}
		
		return true;
	}
	//修改商品信息
	protected boolean update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String pose = request.getParameter("pose");
		//判断：如果是以取消按钮提交则返回购书页面
		if(pose.equals("取消")){
			//如果是以取消按钮提交则进行重定向到初始页面
			this.page(request, response);
			return false;
		}
		String no = request.getParameter("no");
		System.out.println(no);
		HashMap<String, OneBook> books = (HashMap<String, OneBook>) request.getServletContext().getAttribute("allbook");
		OneBook oneBook = books.get(no);
		books.remove(no);
		
		String bookno = request.getParameter("bookno");
		String bookname = request.getParameter("bookname");
		String bookprice = request.getParameter("bookprice");
		String bookcount = request.getParameter("bookcount");
		
		oneBook.setBookno(bookno);
		oneBook.setBookname(bookname);
		oneBook.setBookprice(bookprice);
		oneBook.setBookcount(bookcount);
		
		BookDao bookDao = new BookDao();
		bookDao.updateBookInfo(oneBook);
		books.put(bookno, oneBook);
		request.getServletContext().setAttribute("allbook", books);
		
		this.page(request, response);
		return true;
	}
	//删除某个商品
	protected void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//获取需要删除的商品的编号
		String bookno = request.getParameter("bookno");
		//从session中获取到该用户购物车中该商品的信息
		HttpSession session = request.getSession();
		HashMap<String, BookCart> usercart = (HashMap<String, BookCart>) session.getAttribute("usercart");
		Users users = (Users) session.getAttribute("curuser");
		BookCart bookCart = usercart.get(users.getUsername());
		OneBook oneBook = bookCart.getBookInfo().get(bookno);
		int buyno = bookCart.getBookCnt().get(bookno) + Integer.valueOf(oneBook.getBookcount());
		oneBook.setBookcount(String.valueOf(buyno));
		//修改数据库中的库存信息
		BookDao bookDao = new BookDao();
		bookDao.updateBookInfo(oneBook);
		bookCart.getBookCnt().remove(bookno);
		bookCart.getBookInfo().remove(bookno);
		//修改session中存储的值
		session.setAttribute("usercart", usercart);
		request.getRequestDispatcher("/showcart.jsp").forward(request, response);
	}
	//清空购物车
	protected void clear(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		HashMap<String, BookCart> usercart = (HashMap<String, BookCart>) session.getAttribute("usercart");
		Users users = (Users) session.getAttribute("curuser");
		BookCart bookCart = usercart.get(users.getUsername());
		for (String bookno : bookCart.getBookInfo().keySet()) {
			OneBook oneBook =  bookCart.getBookInfo().get(bookno);
			int buyno = bookCart.getBookCnt().get(bookno) + Integer.valueOf(oneBook.getBookcount());
			oneBook.setBookcount(String.valueOf(buyno));
			//修改数据库中的库存信息
			BookDao bookDao = new BookDao();
			bookDao.updateBookInfo(oneBook);
		}
		usercart.clear();
		session.setAttribute("usercart", usercart);
		response.sendRedirect("clear.jsp");
	}
	//涉及数据页数的操作与处理
	protected void page(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		int pageno = 0;
		if (request.getParameter("pageno") == null) {
			pageno = 1;
		}
		else {
			pageno = Integer.valueOf(request.getParameter("pageno"));
		}
		
		Page page = new Page();
		//获取指定页数据记录
		BookDao bookDao = new BookDao();
		HashMap<String, OneBook> books = bookDao.getPageBooks((pageno-1)*Page.pageSize, Page.pageSize);
		//设置page对象，添加页数信息
		page.setItems(books);
		page.setPageNo(pageno);
		page.setTotalCount(bookDao.getTotalCount());
		//计算总页数
		int pagecount = page.getTotalCount() / Page.pageSize;
		if (page.getTotalCount() % Page.pageSize != 0) {
			pagecount += 1;
		}
		page.setPageTotal(pagecount);
		
		request.setAttribute("pagebook", page);
		request.getRequestDispatcher("/showbook.jsp").forward(request, response);
	}
}
