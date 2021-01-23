package httpSession;

import java.util.HashMap;
import httpSession.GoodsInfo;
import java.util.Map;

//���ﳵ��
public class Shoppingcart {
	//��¼���ﳵ��Ʒ����Ʒ��Ϣ->����
	private Map<String, Integer> items = new HashMap<String, Integer>();
	private Map<String, Double> pr = new HashMap<String, Double>();
	//��¼�ܵ���Ʒ��
	private int number = 0;
	//������Ʒ
	public void addItem(String book, double price){
		//������������1������ֱ�Ӹ�ֵΪ1
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
	//ɾ��һ����Ʒ
	public void delItem(String name){
		if(items.containsKey(name)){
			items.put(name, items.get(name)-1);
			//����Ʒ������С�ڵ���0��ֱ���������Ʒ����Ϣ
			if(items.get(name) <= 0){
				items.remove(name);
				pr.remove(name);
			}
		}
	}
	//����ܵ���Ʒ����
	public int getNumber(){
		return number;
	}
	//��ù��ﳵ��Ʒ
	public Map<String, Integer> getItems(){
		return items;
	}
	//��ù��ﳵ�۸�
		public Map<String, Double> getP(){
			return pr;
		}
	//������Ʒ�ܼ�
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
