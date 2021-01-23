package dao;

import java.sql.ResultSet;
import java.util.HashMap;

import vo.Order;

public class OrderDao {
	//获取所有订单
	public HashMap<String, Order> getAllOrder(){
		HashMap<String, Order> orders = new HashMap<String, Order>();
		String sql = "select * from userorder";
		try {
			BookDBUtil.getCon();
			ResultSet resultSet = BookDBUtil.readData(sql);
			while (resultSet.next()) {
				Order order = new Order();
				order.setOrderno(resultSet.getString("orderno"));
				order.setUsername(resultSet.getString("username"));
				order.setBookname(resultSet.getString("bookname"));
				order.setBookprice(resultSet.getString("bookprice"));
				order.setBuycount(resultSet.getString("buycount"));
				order.setTotalMoney(resultSet.getString("totalMoney"));
				order.setBuydate(resultSet.getString("buydate"));
				order.setAddress(resultSet.getString("address"));
				
				orders.put(resultSet.getString("orderno"), order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			BookDBUtil.closeCon();
		}
		return orders;
	}
	//获取某个用户的订单
	public HashMap<String, Order> getOrderByUsername(String username){
		HashMap<String, Order> orders = new HashMap<String, Order>();
		String sql = "select * from userorder where username='" + username + "'";
		try {
			BookDBUtil.getCon();
			ResultSet resultSet = BookDBUtil.readData(sql);
			while (resultSet.next()) {
				Order order = new Order();
				order.setOrderno(resultSet.getString("orderno"));
				order.setUsername(resultSet.getString("username"));
				order.setBookname(resultSet.getString("bookname"));
				order.setBookprice(resultSet.getString("bookprice"));
				order.setBuycount(resultSet.getString("buycount"));
				order.setTotalMoney(resultSet.getString("totalMoney"));
				order.setBuydate(resultSet.getString("buydate"));
				order.setAddress(resultSet.getString("address"));
				
				orders.put(resultSet.getString("orderno"), order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			BookDBUtil.closeCon();
		}
		return orders;
	}
	//写入记录到数据库
	public void insertOrder(Order order){
		String sql = "insert into userorder(orderno, username, bookname, bookprice, buycount, "
				+ "totalMoney, buydate, address) values('" + order.getOrderno() + "', '" + order.getUsername()
				+ "', '" + order.getBookname() + "', '" + order.getBookprice() + "', '" + order.getBuycount() + "', '"
				+ order.getTotalMoney() + "', '" + order.getBuydate() + "', '" + order.getAddress() + "')";
		try {
			BookDBUtil.getCon();
			BookDBUtil.updateData(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			BookDBUtil.closeCon();
		}
	}
	//获取数据记录条数
	public int getTotalCount(){
		String sql = "select * from userorder";
		int count = 0;
		try {
			BookDBUtil.getCon();
			count = BookDBUtil.getTotalCount(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			BookDBUtil.closeCon();
		}
		return count;
	}
}
