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
		doGet(req, resp);
	}
	
	protected boolean addbook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String pose = request.getParameter("pose");
		//�жϣ��������ȡ����ť�ύ�򷵻ع���ҳ��
		if(pose.equals("ȡ��")){
			//�������ȡ����ť�ύ������ض��򵽳�ʼҳ��
			this.page(request, response);
			return false;
		}
		
		String bookno = request.getParameter("no");
		int number = Integer.parseInt(request.getParameter("number"));
		HashMap<String, OneBook> books = (HashMap<String, OneBook>) request.getServletContext().getAttribute("allbook");
		OneBook oneBook = books.get(bookno);
		int now = Integer.parseInt(oneBook.getBookcount());
		if(now < number){
			request.setAttribute("overCount", "��������������ޣ����������빺��������");
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
			//�޸�ͼ������
			oneBook.setBookcount(String.valueOf(now-number));
			//���޸ĺ�Ľ��д�����ݿ�
			BookDao bookDao = new BookDao();
			bookDao.updateBookInfo(oneBook);
			//�����û��Ĺ��ﳵ���޸�session����
			bookCart.addItem(oneBook, number);
			usercart.put(users.getUsername(), bookCart);
			session.setAttribute("usercart", usercart);
			//��ת�����¼���page���ݣ�����jspҳ�治�ܻ�ȡ������
			this.page(request, response);
		}
		
		return true;
	}
	//�޸���Ʒ��Ϣ
	protected boolean update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String pose = request.getParameter("pose");
		//�жϣ��������ȡ����ť�ύ�򷵻ع���ҳ��
		if(pose.equals("ȡ��")){
			//�������ȡ����ť�ύ������ض��򵽳�ʼҳ��
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
	//ɾ��ĳ����Ʒ
	protected void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//��ȡ��Ҫɾ������Ʒ�ı��
		String bookno = request.getParameter("bookno");
		//��session�л�ȡ�����û����ﳵ�и���Ʒ����Ϣ
		HttpSession session = request.getSession();
		HashMap<String, BookCart> usercart = (HashMap<String, BookCart>) session.getAttribute("usercart");
		Users users = (Users) session.getAttribute("curuser");
		BookCart bookCart = usercart.get(users.getUsername());
		OneBook oneBook = bookCart.getBookInfo().get(bookno);
		int buyno = bookCart.getBookCnt().get(bookno) + Integer.valueOf(oneBook.getBookcount());
		oneBook.setBookcount(String.valueOf(buyno));
		//�޸����ݿ��еĿ����Ϣ
		BookDao bookDao = new BookDao();
		bookDao.updateBookInfo(oneBook);
		bookCart.getBookCnt().remove(bookno);
		bookCart.getBookInfo().remove(bookno);
		//�޸�session�д洢��ֵ
		session.setAttribute("usercart", usercart);
		request.getRequestDispatcher("/showcart.jsp").forward(request, response);
	}
	//��չ��ﳵ
	protected void clear(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		HashMap<String, BookCart> usercart = (HashMap<String, BookCart>) session.getAttribute("usercart");
		Users users = (Users) session.getAttribute("curuser");
		BookCart bookCart = usercart.get(users.getUsername());
		for (String bookno : bookCart.getBookInfo().keySet()) {
			OneBook oneBook =  bookCart.getBookInfo().get(bookno);
			int buyno = bookCart.getBookCnt().get(bookno) + Integer.valueOf(oneBook.getBookcount());
			oneBook.setBookcount(String.valueOf(buyno));
			//�޸����ݿ��еĿ����Ϣ
			BookDao bookDao = new BookDao();
			bookDao.updateBookInfo(oneBook);
		}
		usercart.clear();
		session.setAttribute("usercart", usercart);
		response.sendRedirect("clear.jsp");
	}
	//�漰����ҳ���Ĳ����봦��
	protected void page(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		int pageno = 0;
		if (request.getParameter("pageno") == null) {
			pageno = 1;
		}
		else {
			pageno = Integer.valueOf(request.getParameter("pageno"));
		}
		
		Page page = new Page();
		//��ȡָ��ҳ���ݼ�¼
		BookDao bookDao = new BookDao();
		HashMap<String, OneBook> books = bookDao.getPageBooks((pageno-1)*Page.pageSize, Page.pageSize);
		//����page�������ҳ����Ϣ
		page.setItems(books);
		page.setPageNo(pageno);
		page.setTotalCount(bookDao.getTotalCount());
		//������ҳ��
		int pagecount = page.getTotalCount() / Page.pageSize;
		if (page.getTotalCount() % Page.pageSize != 0) {
			pagecount += 1;
		}
		page.setPageTotal(pagecount);
		
		request.setAttribute("pagebook", page);
		request.getRequestDispatcher("/showbook.jsp").forward(request, response);
	}
}
