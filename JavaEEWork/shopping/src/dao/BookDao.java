package dao;

import java.sql.ResultSet;
import java.util.HashMap;

import vo.OneBook;

public class BookDao {
	//��ѯ���ݿ���������
	public HashMap<String, OneBook> getAllBooks(){
		HashMap<String, OneBook> books = new HashMap<String, OneBook>();
		String sql = "select * from books";
		try {
			BookDBUtil.getCon();
			ResultSet resultSet = BookDBUtil.readData(sql);
			while (resultSet.next()) {
				OneBook book = new OneBook();
				book.setBookno(resultSet.getString("booknum"));
				book.setBookname(resultSet.getString("bookname"));
				book.setBookprice(resultSet.getString("bookprice"));
				book.setBookcount(resultSet.getString("bookcount"));
				
				books.put(resultSet.getString("booknum"), book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			BookDBUtil.closeCon();
		}
		return books;
	}
	//����ͼ����ɾ�����ݿ��¼
	public boolean deleteByBookno(String no){
		String sql = "delete from books where booknum = " + no;
		try {
			BookDBUtil.getCon();
			return BookDBUtil.deleteData(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			BookDBUtil.closeCon();
		}
	}
	//����һ��ͼ����Ϣ��¼�����ݿ�
	public boolean insertOneBook(String no, String name, String price, String count){
		OneBook book = new OneBook();
		book.setBookno(no);
		book.setBookname(name);
		book.setBookprice(price);
		book.setBookcount(count);
		try {
			BookDBUtil.getCon();
			return BookDBUtil.addBook(book);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			BookDBUtil.closeCon();
		}
	}
	//��ȡ��ǰҳ����
	public HashMap<String, OneBook> getPageBooks(int begin, int size){
		HashMap<String, OneBook> books = new HashMap<String, OneBook>();
		String sql = "select * from books limit " + begin + ", " + size;
		try {
			BookDBUtil.getCon();
			ResultSet resultSet = BookDBUtil.readData(sql);
			while (resultSet.next()) {
				OneBook book = new OneBook();
				book.setBookno(resultSet.getString("booknum"));
				book.setBookname(resultSet.getString("bookname"));
				book.setBookprice(resultSet.getString("bookprice"));
				book.setBookcount(resultSet.getString("bookcount"));
				
				books.put(resultSet.getString("booknum"), book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			BookDBUtil.closeCon();
		}
		return books;
	}
	//�޸�ͼ����Ϣ
	public void updateBookInfo(OneBook oneBook){
		String sql = "replace into books(booknum, bookname, bookprice, bookcount) "
				+ "values('" +  oneBook.getBookno()+ "', '" + oneBook.getBookname() + "', '" + 
				oneBook.getBookprice() + "', '" + oneBook.getBookcount() + "')";
		try {
			BookDBUtil.getCon();
			BookDBUtil.updateData(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			BookDBUtil.closeCon();
		}
	}
	
	//��ȡ�ܵ���������
	public int getTotalCount(){
		String sql = "select * from books";
		int pages = 0;
		try {
			BookDBUtil.getCon();
			pages = BookDBUtil.getTotalCount(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			BookDBUtil.closeCon();
		}
		return pages;
	}
}
