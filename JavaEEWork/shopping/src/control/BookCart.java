package control;

import java.util.HashMap;
import java.util.Map;

import vo.OneBook;

public class BookCart {
	private HashMap<String, Integer> bookCnt = new HashMap<String, Integer>();
	private HashMap<String, OneBook> bookInfo = new HashMap<String, OneBook>();
	private int booknum = 0;
	private double totalMoney = 0;
	
	//���ﳵ���һ���鼮
	public void addItem(OneBook book, int number){
		String bookno = book.getBookno();
		//�ж��Ƿ���ڵ�ǰ��ӵ��鱾��������������һ������������1�Ҽ����Ӧ��ͼ����Ϣ
		if (bookCnt.containsKey(bookno)) {
			bookCnt.put(bookno, bookCnt.get(bookno)+number);
		}
		else {
			bookCnt.put(bookno, number);
			bookInfo.put(bookno, book);
		}
	}
	//ɾ�����ﳵ��һ���鼮
	public void deleteItem(String bookno){
		//����ָ�������ŲŽ���ɾ��������������1��������Ϊ0����ɾ���鱾��Ϣ
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
