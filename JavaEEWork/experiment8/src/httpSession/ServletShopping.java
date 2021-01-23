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
		//书单列表
		String[] bookName = {"大地之灯", "我与地坛", "沉睡的人鱼之家", "有问集", "灯下尘", "扶轮问路"};
		double[] price = {58.00, 39.00, 64.00, 28.00, 26.00, 29.00};
		String[] booksSelected;
		String name;
		double money;
		
		//获取当前会话
		HttpSession session = req.getSession();
		//将会话ID存入cookies中
		Cookie neCoo = new Cookie("sessionId", session.getId());
		neCoo.setMaxAge(-1);
		resp.addCookie(neCoo);
		//获取购物车，若没有则新建
		Shoppingcart cart = (Shoppingcart)session.getAttribute("cart");
		if(cart == null){
			cart = new Shoppingcart();
			session.setAttribute("cart", cart);
		}
		//将新选择的书籍添加到购物车
		booksSelected = req.getParameterValues("item");
		if(booksSelected != null){
			for (String bookIndex : booksSelected) {
				name = bookName[Integer.parseInt(bookIndex)];
				money = price[Integer.parseInt(bookIndex)];
				cart.addItem(name, money);
			}
		}
		//输出购物车内容
		out.print("<html><body><h2>您的购物车共" + cart.getNumber() + "个商品</h2>");
		out.print("<hr>");
		out.print("您选购的以下书籍已被加入购物车: <br>");
		for (String bookIndex : booksSelected) {
			name = bookName[Integer.parseInt(bookIndex)];
			money = price[Integer.parseInt(bookIndex)];
			out.print("书名：《" + name + "》   价格：￥" + money + "<br>");
		}
		out.print("<hr>");
		out.print("<a href='shopping.jsp'>继续购物</a>&nbsp&nbsp|&nbsp&nbsp");
		out.print("<a href='showCart.jsp'>查看购物车</a>");
		out.print("</body></html>");
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(req, resp);
	}
}
