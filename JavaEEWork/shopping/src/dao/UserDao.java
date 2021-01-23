package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import vo.Users;

public class UserDao {
	//数据库连接信息
	private static String driverName = null;
	private static String url = null;
	private static String user = null;
	private static String pwd = null;
	private static Connection connection = null;
	private static Statement statement = null;
	private static PreparedStatement ps = null;
	private static ResultSet resultSet = null;
	
	static {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("bookjdbc");
		driverName = resourceBundle.getString("driverName");
		url = resourceBundle.getString("url");
		user = resourceBundle.getString("user");
		pwd = resourceBundle.getString("pwd");
	}
	
	public void userWrite(String name, String pwd, String addr, String paypwd){
		String sql = "insert into user(username, userpwd, admin, address, paypwd) "
				+ "values(?, ?, ?, ?, ?)";
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(url, user, UserDao.pwd);
			ps = connection.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, pwd);
			ps.setString(3, "no");
			ps.setString(4, addr);
			ps.setString(5, paypwd);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				this.closeLink();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Users check(String name) {
		Users users = new Users();;
		String sql = "select * from user";
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(url, user, pwd);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				if(resultSet.getString("username").equals(name)){
					users.setUsername(resultSet.getString("username"));
					users.setUserpwd(resultSet.getString("userpwd"));
					users.setAuthority(resultSet.getString("admin"));
					users.setPaypwd(resultSet.getString("paypwd"));
					users.setAddress(resultSet.getString("address"));
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				this.closeLink();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return users;
	}
	
	public void closeLink() throws SQLException{
		if (resultSet != null) {
			resultSet.close();
		}
		if (statement != null) {
			statement.close();
		}
		if (ps != null) {
			ps.close();
		}
		if (connection != null) {
			connection.close();
		}
	}
}
