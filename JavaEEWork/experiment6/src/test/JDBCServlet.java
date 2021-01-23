package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JDBCServlet extends HttpServlet {
	private static String driver = null;
	private static String url = null;
	private static String user = null;
	private static String pwd = null;
	private static Connection con = null;
	private static PreparedStatement pstm = null;
	private static ResultSet res = null;
	
	@Override
	public void init() throws ServletException {
		driver = this.getInitParameter("driver");
		url = this.getInitParameter("url");
		user = this.getInitParameter("user");
		pwd = this.getInitParameter("pwd");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		String insert = "insert into stu values(?, ?)";
		String sql = "select * from stu";
		
		try{
			//获取连接
			con = this.getCon();
			//插入两条记录
			this.DBInsert(insert, "Bob", 21);
			this.DBInsert(insert, "Mary", 19);
			//数据库查询
			res = this.DBRead(sql);
			while(res.next()){
				System.out.println("name: " + res.getString(1) + ", age: " + res.getString(2));
			}
			//关闭所有连接
			this.endClose();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	//数据库连接获取
	public Connection getCon(){
		try{
			Class.forName(driver);
			con = (Connection)DriverManager.getConnection(url, user, pwd);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	//数据库插入
	public void DBInsert(String sql, String name, int age) throws SQLException {
		pstm = con.prepareStatement(sql);
		pstm.setString(2, name);
		pstm.setInt(1, age);
		pstm.executeUpdate();
	}
	//数据库查询
	public ResultSet DBRead(String sql) throws SQLException {
		pstm = con.prepareStatement(sql);
		res = pstm.executeQuery();
		return res;
	}
	//数据库连接关闭
	public void endClose() throws SQLException {
		res.close();
		pstm.close();
		con.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
