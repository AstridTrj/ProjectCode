package stuPrint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class ControlCon {
	private static Connection connection = null;
	private static String driverName = null;
	private static String url = null;
	private static String user = null;
	private static String pwd = null;
	
	static {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("jdbc");
		driverName = resourceBundle.getString("driverName");
		url = resourceBundle.getString("url");
		user = resourceBundle.getString("user");
		pwd = resourceBundle.getString("pwd");
	}
	//获取连接
	public static Connection getCon() {
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(url, user, pwd);
			System.out.println("数据库连接成功.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	//关闭数据库连接
	public static void closeCon() {
		try {
			if(connection != null)
				connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
