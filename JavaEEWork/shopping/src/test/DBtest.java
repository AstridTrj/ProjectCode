package test;

import java.util.HashMap;
import dao.BookDao;
import vo.OneBook;

public class DBtest {

	public static boolean trytest() {
		try {
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			System.out.println("sgiosghege");
		}
	}
	
	public static void main(String[] args) {
		BookDao bookDao = new BookDao();
		OneBook oneBook = new OneBook();
		oneBook.setBookname("Жёвт");
		oneBook.setBookno("016");
		oneBook.setBookcount("467");
		oneBook.setBookprice("49.60");
		bookDao.updateBookInfo(oneBook);
	}

}
