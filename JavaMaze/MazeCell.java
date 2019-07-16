package clove;

/*************************************
 * @author 汤荣杰
 * 迷宫单元格类（挨着的9个位一个单元格）
 * 每个单元格的属性，功能等
 *************************************/

//迷宫单元格
public class MazeCell{
	//是否被访问
	private boolean isVisited = false;
	//各个方向是否有墙
	private boolean upWall = true;
	private boolean downWall = true;
	private boolean leftWall = true;
	private boolean rightWall = true;
	
	public boolean isVisited() {
		return isVisited;
	}
	
	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}
	
	public boolean isUp() {
		return upWall;
	}
	
	public void setUp(boolean upWall) {
		this.upWall = upWall;
	}
	
	public boolean isDown() {
		return downWall;
	}
	
	public void setDown(boolean downWall) {
		this.downWall = downWall;
	}
	
	public boolean isLeft() {
		return leftWall;
	}
	
	public void setLeft(boolean leftWall) {
		this.leftWall = leftWall;
	}
	
	public boolean isRight() {
		return rightWall;
	}
	
	public void setRight(boolean rightWall) {
		this.rightWall = rightWall;
	}
}

