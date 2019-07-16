package clove;

/************************************
 * @author 汤荣杰
 * 坐标点类，即展示的迷宫中每个点的坐标
 ************************************/

	//坐标点类
public class Box{
	private int x;
	private int y;
	
	public Box(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}
