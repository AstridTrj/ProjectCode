package vo;

public class Order {
	private String orderno;
	private String username;
	private String bookname;
	private String bookprice;
	private String buycount;
	private String totalMoney;
	private String buydate;
	private String address;
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBookprice() {
		return bookprice;
	}
	public void setBookprice(String bookprice) {
		this.bookprice = bookprice;
	}
	public String getBuycount() {
		return buycount;
	}
	public void setBuycount(String buycount) {
		this.buycount = buycount;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getBuydate() {
		return buydate;
	}
	public void setBuydate(String buydate) {
		this.buydate = buydate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBookname() {
		return bookname;
	}
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
}
