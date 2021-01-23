package control;

import java.util.HashMap;
import java.util.Map;

import vo.OneBook;

public class BookCart {
	private HashMap<String, Integer> bookCnt = new HashMap<String, Integer>();
	private HashMap<String, OneBook> bookInfo = new HashMap<String, OneBook>();
	private int booknum = 0;
	private double totalMoney = 0;
	
	//向购物车添加一本书籍
	public void addItem(OneBook book, int number){
		String bookno = book.getBookno();
		//判断是否存在当前添加的书本，若存在数量加一，否则数量置1且加入对应的图书信息
		if (bookCnt.containsKey(bookno)) {
			bookCnt.put(bookno, bookCnt.get(bookno)+number);
		}
		else {
			bookCnt.put(bookno, number);
			bookInfo.put(bookno, book);
		}
	}
	//删除购物车的一本书籍
	public void deleteItem(String bookno){
		//存在指定的书编号才进行删除，对其数量减1，若数量为0，则删除书本信息
		if (bookCnt.containsKey(bookno)) {
			bookCnt.put(bookno, bookCnt.get(bookno)-1);
			if(bookCnt.get(bookno) <= 0){
				bookCnt.remove(bookno);
				bookInfo.remove(bookno);
			}
		}
	}
	public HashMap<String, Integer> getBookCnt() {
		return bookCnt;
	}
	public void setBookCnt(HashMap<String, Integer> bookCnt) {
		this.bookCnt = bookCnt;
	}
	public HashMap<String, OneBook> getBookInfo() {
		return bookInfo;
	}
	public void setBookInfo(HashMap<String, OneBook> bookInfo) {
		this.bookInfo = bookInfo;
	}
	public int getBooknum() {
		this.booknum = this.bookInfo.size();
		return this.booknum;
	}
	public void setBooknum(int booknum) {
		this.booknum = booknum;
	}
	public double getTotalMoney() {
		totalMoney = 0;
		for (String bookno : bookInfo.keySet()) {
			int count = bookCnt.get(bookno);
			double price = Double.valueOf(bookInfo.get(bookno).getBookprice());
			totalMoney += count * price;
		}
		totalMoney = (double) Math.round(totalMoney * 100) / 100;
		return totalMoney;
	}
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
	
}
