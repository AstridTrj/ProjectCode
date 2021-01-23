package vo;

import java.util.HashMap;

public class Page {
	private int pageNo;
	private int pageTotal;
	private int totalCount;
	public static int pageSize = 8;
	private HashMap<String, OneBook> items;
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageTotal() {
		return pageTotal;
	}
	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public HashMap<String, OneBook> getItems() {
		return items;
	}
	public void setItems(HashMap<String, OneBook> items) {
		this.items = items;
	}
}
