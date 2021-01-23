package stuPrint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import stuPrint.ControlCon;

public class StuDao {
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private PreparedStatement ps = null;
	
	//获取所有图书信息
	public HashMap<String, StuBean> getAllInfo() {
		HashMap<String, StuBean> students = new HashMap<String, StuBean>();
		String sql = "select * from stu";
		try {
			connection = ControlCon.getCon();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				StuBean stu = new StuBean();
				stu.setName(resultSet.getString("name"));
				stu.setNo(Integer.parseInt(resultSet.getString("no")));
				stu.setSex(resultSet.getString("sex"));
				students.put(resultSet.getString("no"), stu);
			}
			ControlCon.closeCon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return students;
	}
}
