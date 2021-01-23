package httpSession;

import java.util.HashMap;
import httpSession.GoodsInfo;
import java.util.Map;

//购物车类
public class Shoppingcart {
	//记录购物车物品：物品信息->数量
	private Map<String, Integer> items = new HashMap<String, Integer>();
	private Map<String, Double> pr = new HashMap<String, Double>();
	//记录总的商品数
	private int number = 0;
	//加入商品
	public void addItem(String book, double price){
		//存在则数量加1，否则直接赋值为1
		if(items.containsKey(book)){
			int count = items.get(book);
			items.put(book, count+1);
			number++;
		}
		else{
			items.put(book, 1);
			pr.put(book, price);
			number++;
		}
	}
	//删除一个商品
	public void delItem(String name){
		if(items.containsKey(name)){
			items.put(name, items.get(name)-1);
			//当商品的数量小于等于0则直接清除该商品的信息
			if(items.get(name) <= 0){
				items.remove(name);
				pr.remove(name);
			}
		}
	}
	//获得总的商品数量
	public int getNumber(){
		return number;
	}
	//获得购物车商品
	public Map<String, Integer> getItems(){
		return items;
	}
	//获得购物车价格
		public Map<String, Double> getP(){
			return pr;
		}
	//计算物品总价
	public double getTotalPrice() {
		double res = 0;
		Map<String, Integer> gs = new HashMap<String, Integer>();
		gs = this.getItems();
		for (String key : gs.keySet()) {
			double money = pr.get(key);
			int num = gs.get(key);
			res += money * num;
		}
		return res;
	}
}
