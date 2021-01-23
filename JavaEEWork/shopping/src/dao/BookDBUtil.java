package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import vo.OneBook;

public class BookDBUtil {
	//���ݿ�������Ϣ
	private static String driverName = null;
	private static String url = null;
	private static String user = null;
	private static String pwd = null;
	private static Connection connection = null;
	//���ݿ������Ϣ
	private static Statement statement = null;
	private static PreparedStatement ps = null;
	private static ResultSet resultSet = null;
	
	//��Ϣ��ʼ��
	static {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("bookjdbc");
		driverName = resourceBundle.getString("driverName");
		url = resourceBundle.getString("url");
		user = resourceBundle.getString("user");
		pwd = resourceBundle.getString("pwd");
	}
	//��ȡ����
	public static void getCon() {
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(url, user, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//��ȡ�ܵ���������
	public static int getTotalCount(String sql){
		int count = 0;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.last()){
				count = resultSet.getRow();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	//��ѯ����
	public static ResultSet readData(String sql){
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return resultSet;
	}
	
	//��������(����һ����)
	public static boolean addBook(OneBook book) throws SQLException {
		String sql = "insert into books(booknum, bookname, bookprice, bookcount) values(?, ?, ?, ?)";
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, book.getBookno());
			ps.setString(2, book.getBookname());
			ps.setString(3, book.getBookprice());
			ps.setString(4, book.getBookcount());
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	//ɾ������(��������ɾ���鼮)
	public static boolean deleteData(String sql) {
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	//������Ϣ
	public static void updateData(String sql){
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//�ر����ݿ����Ӽ���ز���
	public static void closeCon() {
		try {
			if(resultSet != null)
				resultSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(statement != null)
				statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(ps != null)
				ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(connection != null)
				connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
