package clove;

/*************************************
 * @author ���ٽ�
 * �Թ���Ԫ���ࣨ���ŵ�9��λһ����Ԫ��
 * ÿ����Ԫ������ԣ����ܵ�
 *************************************/

//�Թ���Ԫ��
public class MazeCell{
	//�Ƿ񱻷���
	private boolean isVisited = false;
	//���������Ƿ���ǽ
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

