package httpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import httpSession.Shoppingcart;

@WebServlet(name="ServletShopping", urlPatterns="/ServletShopping")
public class ServletShopping extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		//�鵥�б�
		String[] bookName = {"���֮��", "�����̳", "��˯������֮��", "���ʼ�", "���³�", "������·"};
		double[] price = {58.00, 39.00, 64.00, 28.00, 26.00, 29.00};
		String[] booksSelected;
		String name;
		double money;
		
		//��ȡ��ǰ�Ự
		HttpSession session = req.getSession();
		//���ỰID����cookies��
		Cookie neCoo = new Cookie("sessionId", session.getId());
		neCoo.setMaxAge(-1);
		resp.addCookie(neCoo);
		//��ȡ���ﳵ����û�����½�
		Shoppingcart cart = (Shoppingcart)session.getAttribute("cart");
		if(cart == null){
			cart = new Shoppingcart();
			session.setAttribute("cart", cart);
		}
		//����ѡ����鼮��ӵ����ﳵ
		booksSelected = req.getParameterValues("item");
		if(booksSelected != null){
			for (String bookIndex : booksSelected) {
				name = bookName[Integer.parseInt(bookIndex)];
				money = price[Integer.parseInt(bookIndex)];
				cart.addItem(name, money);
			}
		}
		//������ﳵ����
		out.print("<html><body><h2>���Ĺ��ﳵ��" + cart.getNumber() + "����Ʒ</h2>");
		out.print("<hr>");
		out.print("��ѡ���������鼮�ѱ����빺�ﳵ: <br>");
		for (String bookIndex : booksSelected) {
			name = bookName[Integer.parseInt(bookIndex)];
			money = price[Integer.parseInt(bookIndex)];
			out.print("��������" + name + "��   �۸񣺣�" + money + "<br>");
		}
		out.print("<hr>");
		out.print("<a href='shopping.jsp'>��������</a>&nbsp&nbsp|&nbsp&nbsp");
		out.print("<a href='showCart.jsp'>�鿴���ﳵ</a>");
		out.print("</body></html>");
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(req, resp);
	}
}
