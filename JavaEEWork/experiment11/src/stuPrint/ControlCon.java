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
	//��ȡ����
	public static Connection getCon() {
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(url, user, pwd);
			System.out.println("���ݿ����ӳɹ�.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	//�ر����ݿ�����
	public static void closeCon() {
		try {
			if(connection != null)
				connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
