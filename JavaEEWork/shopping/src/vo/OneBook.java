package vo;

public class OneBook {
	//ͼ���ţ�ͼ�����֣�ͼ��۸�ͼ��������
	private String bookno = null;
	private String bookname = null;
	private String bookprice = null;
	private String bookcount = null;
	
	public OneBook() {
	}
	
	public OneBook(String no, String name, String price, String count) {
		this.bookno = no;
		this.bookname = name;
		this.bookprice = price;
		this.bookcount = count;
	}
	
	public String getBookno() {
		return bookno;
	}
	public void setBookno(String bookno) {
		this.bookno = bookno;
	}
	public String getBookname() {
		return bookname;
	}
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	public String getBookprice() {
		return bookprice;
	}
	public void setBookprice(String bookprice) {
		this.bookprice = bookprice;
	}
	public String getBookcount() {
		return bookcount;
	}
	public void setBookcount(String bookcount) {
		this.bookcount = bookcount;
	}
}
