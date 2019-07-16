package clove;

/*****************************************************************
 * @author ���ٽ�
 * ����Ϊ�Թ������࣬������Ӧ�Ĺ��ܽ����˷ֿ���д
 * �����а������Թ�������Ѱ·�����·����ʼ���ȹ����Լ���Ӧ�Ķ���
******************************************************************/
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

//�Թ���
public class MazePane extends GridPane{
	//�ĸ�������
	//0-����  1-����  2-����   3-����
	private final static int dirUp = 0;
	private final static int dirDown = 1;
	private final static int dirLeft = 2;
	private final static int dirRight = 3;
	
	//�Ƿ���ǽ��·��·���ı�ʾ
	//0��ʾδ�߹���·�� 1��ʾǽ�� 2��ʾ·��
	private final static int Wall = 1;
	private final static int Empty = 0;
	private final static int Path = 2;
	
	//�Թ����
	private int width;
	//�Թ��߶�
	private int height;
	//�Թ��г�ʼ��ʱ�Ŀյĵ�Ԫ������
	private MazeCell[][] mazecell;
	//�Թ�����
	private int[][] maze;

	
	//�洢�ڱ�������Ѱ�ҹ����е����꣬����·��
	private ArrayList<Box> createList = new ArrayList<Box>();
	private ArrayList<Box> seekList = new ArrayList<Box>();
	private ArrayList<Box> travelList = new ArrayList<Box>();
	//ʱ���ᶯ��
	private Timeline timeline;
	//·���������
	private int m = 0;
	//���г���ʱ���ж����ļ���,��p=0ʱ����ִ�иö���������ʼ����Ҫ�Ķ���
	private static int p = 0;
	
	//���캯�������ݸ������ȺͿ�ȴ���һ���Թ�
	public MazePane(int width, int height) {
		this.setHeight(600);
		this.setWidth(600);
		this.width = width;
		this.height = height;
		this.mazecell = new MazeCell[height][width];
		this.maze = new int[2 * height + 1][2 * width + 1];
		
		//ѭ�����γ�ʼ��ÿ����Ԫ��
		for(int i = 0; i < this.height; i++)
			for(int j = 0; j < this.width; j++)
				mazecell[i][j] = new MazeCell();
		
		//����ÿ��������ӵĴ�С
		int dw = (int)this.getWidth() / (2 * width);
		int dh = (int)this.getHeight() / (2 * height);
		for(int i = 0; i < 2 * this.height + 1; i++) {
			for(int j = 0; j < 2 * this.width + 1; j++){
				//������Ϊȫ����ǽ��������ʱȫ��չʾΪǽ
				ImageView re = new ImageView(new Image("file:mazePicture/wall.jpg"));
				re.setFitWidth(dw);
				re.setFitHeight(dh);
				//��ӵ���ǰ���
				add(re, j, i);
			}
		}
		//���뵭������������һ��������������ֵ��0�䵽1��ѭ������Ϊ1��
		FadeTransition ft = new FadeTransition(Duration.millis(4200), this);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.setCycleCount(1);
		ft.play();
		
		//���p=0��ִ�г�ʼ���еĶ�������������
		if(p == 0)
			animation();
		p++;
	}
	
	//���ж���
	protected void animation() {
		//���뵭������
		FadeTransition ft = new FadeTransition(Duration.millis(5000), this);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.setCycleCount(1);
		
		//��ת��������ת�Ƕ�Ϊ720�ȣ�����һ��
		RotateTransition ro = new RotateTransition(Duration.millis(3000), this);
		ro.setByAngle(720);
		ro.setCycleCount(1);
		
		//���Ŷ���
		ScaleTransition sc = new ScaleTransition(Duration.millis(4000), this);
		sc.setFromX(0);
		sc.setFromY(0);
		sc.setToX(1);
		sc.setToY(1);
		sc.setCycleCount(1);
		
		//���ж���ִ�У������뵭������ת�����Ŷ�������ִ��
		ParallelTransition pa = new ParallelTransition(ft, ro, sc);
		pa.setCycleCount(1);
		pa.play();
	}
	
	//�ж�dir�����Ƿ���δ�����ʵĵ�Ԫ��
	protected boolean ifAroundHasNotVisited(int x, int y, int dir){
		boolean isAroundVisited = false;
		switch(dir){
		case dirUp:
			//�Ƿ��ڱ߽�
			if(x <= 0)
				isAroundVisited = true;
			//�����Ǳ߽磬�򷵻ظ÷���Ԫ���Visited����
			else
				isAroundVisited = mazecell[x - 1][y].isVisited();
			break;
		case dirRight:
			if(y >= width - 1)
				isAroundVisited = true;
			else
				isAroundVisited = mazecell[x][y + 1].isVisited();
			break;
		case dirDown:
			if(x >= height - 1)
				isAroundVisited = true;
			else
				isAroundVisited = mazecell[x + 1][y].isVisited();
			break;
		case dirLeft:
			if(y <= 0)
				isAroundVisited = true;
			else
				isAroundVisited = mazecell[x][y - 1].isVisited();
			break;
		}
		
		return !isAroundVisited;
	}
	
	//���ط���ifAroundHasNotVisited����
	//�����Χ������һ����Ԫ��δ�����ʣ��򷵻�true����ʾ�ɼ�������
	protected boolean ifAroundHasNotVisited(int x, int y){
		return (this.ifAroundHasNotVisited(x, y, dirUp) || this.ifAroundHasNotVisited(x, y, dirRight)
				|| this.ifAroundHasNotVisited(x, y, dirDown) || this.ifAroundHasNotVisited(x, y, dirLeft));
	}
	
	//������� һ����������÷���ȫ�������������²���
	protected int getRandomDir(int x, int y) {
		int dir = -1;
		Random rand = new Random();
		
		if(ifAroundHasNotVisited(x, y)){
			do{
				dir = rand.nextInt(4);
			}while(!ifAroundHasNotVisited(x, y, dir));
		}
		
		return dir;
	}
	
	//����dirͨ���޸��Թ���Ԫ�������ȥ������·��֮���ǽ�����γ��Թ�Ч��
	protected void pushWall(int x, int y, int dir){
		switch(dir){
		case dirUp:
			//ȥ�����Ϸ�ǽ
			mazecell[x][y].setUp(false);
			//ȥ�����·�ǽ
			mazecell[x - 1][y].setDown(false);
			//��¼�õ㼰����һ���ߵ�
			createList.add(new Box(2 * x + 1, 2 * y + 1));
			createList.add(new Box(2 * x, 2 * y + 1));
			break;
		case dirRight:
			mazecell[x][y].setRight(false);
			mazecell[x][y + 1].setLeft(false);
			createList.add(new Box(2 * x + 1, 2 * y + 1));
			createList.add(new Box(2 * x + 1, 2 * y + 2));
			break;
		case dirDown:
			mazecell[x][y].setDown(false);
			mazecell[x + 1][y].setUp(false);
			createList.add(new Box(2 * x + 1, 2 * y + 1));
			createList.add(new Box(2 * x + 2, 2 * y + 1));
			break;
		case dirLeft:
			mazecell[x][y].setLeft(false);
			mazecell[x][y - 1].setRight(false);
			createList.add(new Box(2 * x + 1, 2 * y + 1));
			createList.add(new Box(2 * x + 1, 2 * y));
			break;
		}
	}
	
	//ͨ�������������ԭ��������������Թ�
	public void travelAllCell(){
		//�ӣ�0��0����ʼ
		int x = 0;
		int y = 0;
		//X����ջ���洢����X����
		Stack<Integer> stackX = new Stack<Integer>();
		//Y����ջ���洢����Y����
		Stack<Integer> stackY = new Stack<Integer>();
		
		do{
			MazeCell p = mazecell[x][y];
			//�Ƿ񱻷��ʣ�δ����������Ϊ�ѱ�����
			if( !p.isVisited() ){
				p.setVisited(true);
			}
			
			//����õ���Χ��δ�����ʵĵ���ִ��if�µ����
			if(ifAroundHasNotVisited(x, y)){
				//��ȡ�����ķ���
				int dir = this.getRandomDir(x, y);
				//ȥ�����ߵ�Ԫ��֮���ǽ
				this.pushWall(x, y, dir);
				//����ǰ��װ��ջ�����Ըõ����ѭ��
				stackX.add(x);
				stackY.add(y);
				
				//���ݷ��򣬽��˷���ĵ���Ϊ��ǰ�㣬������仯
				switch(dir){
				case dirUp:
					x--;
					break;
				case dirRight:
					y++;
					break;
				case dirDown:
					x++;
					break;
				case dirLeft:
					y--;
					break;
				}
			}
			else{
				//��¼���ɹ����е�·��������������Ⱥ�˳���������ɶ���
				createList.add(new Box(2 * x + 1, 2 * y + 1));
				//����ķ�����Ѿ�ȫ���������򵯳���ǰ�����꣬�����
				x = stackX.pop();
				y = stackY.pop();
			}
		}while( !stackX.isEmpty());
	}
	
	//����֮�󣬸������ԣ���ÿ���Թ���������Ϊ��Ӧ��ǽ��·����·��
	public void createMaze(){
		this.travelAllCell();
		for(int j = 0; j < 2 * width + 1; j++)
			maze[0][j] = Wall;
		for(int i = 0; i < height; i++){
			maze[2 * i + 1][0] = Wall;
			for(int j = 0; j < width; j++){
				maze[2 * i + 1][2 * j + 1] = Empty;
				//���ҷ���ǽ�Ƿ��ȥ��
				if(mazecell[i][j].isRight())
					maze[2 * i + 1][2 * j + 2] = Wall;
				else
					maze[2 * i + 1][2 * j + 2] = Empty;
			}
			//���������Ǵ����ң����ϵ��£��ʲ��ж�����Ϸ���ǽ
			maze[2 * i + 2][0] = Wall;
			for(int j = 0; j < width; j++){
				//���·���ǽ�Ƿ��ȥ��
				if(mazecell[i][j].isDown())
					maze[2 * i + 2][2 * j + 1] = Wall;
				else
					maze[2 * i + 2][2 * j + 1] = Empty;
				maze[2 * i + 2][2 * j + 2] = Wall;
			}
		}
		
		//����-���ø���Ŀգ��Դﵽ����·��
		//ԭ���ж��Ƿ����4������ǽ�����ǣ����ڳ�һ��������ÿ��ֻ��һ��
		for(int i = 1; i < 2 * height; i++) {
			for(int j = 1; j < 2 * width - 3; j++) {
				if(maze[i][j] == Wall && maze[i][j + 1] == Wall && 
						maze[i][j + 2] == Wall && maze[i][j + 3] == Wall) {
					if(maze[i + 1][j + 2] == Empty && maze[i - 1][j + 2] == Empty) {
						maze[i][j + 2] = Empty;
						createList.add(new Box(i, j + 2));
						break;
					}
					if(maze[i + 1][j + 1] == Empty && maze[i - 1][j + 1] == Empty) {
						maze[i][j + 1] = Empty;
						createList.add(new Box(i, j + 1));
						break;
					}
				}
			}
		}
		//�������ɶ���
		generationAnimation();
	}
	
	//����������Ϊwall���wallͼƬ��empty��pathͬ��
	public void printInPane() {
		int dh = (int)600 / (2 * height);
		int dw = (int)600 / (2 * width);
		for(int i = 0; i < 2 * height + 1; i++) {
			for(int j = 0; j < 2 * width + 1; j++) {
				//����������������ǽ�������ǽ��ͼƬ
				if(maze[i][j] == Wall) {
					ImageView re = new ImageView(new Image("file:mazePicture/wall.jpg"));
					re.setFitWidth(dw);
					re.setFitHeight(dh);
					add(re, j, i);
				}
				else if(maze[i][j] == Empty){
					ImageView re = new ImageView(new Image("file:mazePicture/empty.png"));
					re.setFitWidth(dw);
					re.setFitHeight(dh);
					add(re, j, i);
				}
				else {
					Circle cr = new Circle(dh / 2 - 2);
					cr.setStroke(Color.WHITE);
					cr.setFill(Color.WHITE);
					add(cr, j, i);
				}
			}
		}
	}
	
	//�Թ����ɶ��������ݼ�¼��·������������Դﵽ����Ч��
	protected void generationAnimation() {
		int dh = (int)600 / (2 * height);
		int dw = (int)600 / (2 * width);
		EventHandler<ActionEvent> handler = e -> {
			ImageView re = new ImageView(new Image("file:mazePicture/empty.png"));
			re.setFitWidth(dw);
			re.setFitHeight(dh);
			add(re, createList.get(m).getY(), createList.get(m).getX());
			
			//���һ�����뵭������
			FadeTransition f = new FadeTransition(Duration.millis(1000), re);
			f.setFromValue(0);
			f.setToValue(1);
			f.setCycleCount(1);
			f.play();
			m++;
			
		};
		
		timeline = new Timeline(new KeyFrame(Duration.millis(30), handler));
		timeline.setCycleCount(createList.size());
		timeline.play();
	}
	
	//Ѱ��·��ʱget��һ��·����������õ����һ������δ�ߣ��ͼ�¼��ǰ���򲢷���
	//���´�Ѱ·ʱ���Ͱ��շ��صķ���Ѱ·
	protected int getNextPathDir(int x, int y){
		int dir = -1;
		if(maze[x][y + 1] == Empty)
			dir = dirRight;
		else if(maze[x - 1][y] == Empty)
			dir = dirUp;
		else if(maze[x][y - 1] == Empty)
			dir = dirLeft;
		else if(maze[x + 1][y] == Empty)
			dir = dirDown;
		return dir;
	}
	
	//Ѱ��һ��·����ͨ���������������ԭ�����أ����ҳ�һ��·��
	public void findAPath(){
		//�����ǰ��廹����������ִ�ж���������ֹͣ�ö���
		if(timeline != null)
			timeline.pause();
		seekList.clear();
		//��ս������
		printInPane();
		
		int x = 1;
		int y = 1;
		Stack<Integer> stackX = new Stack<Integer>();
		Stack<Integer> stackY = new Stack<Integer>();
		do {
			int dir = this.getNextPathDir(x, y);
			if(dir == -1) {
				maze[x][y] = -1;
				//��¼���������������е�·����
				seekList.add(new Box(x, y));
				x = stackX.pop();
				y = stackY.pop();
			}
			else {
				maze[x][y] = Path;
				stackX.add(x);
				stackY.add(y);
				seekList.add(new Box(x, y));
				switch(dir) {
				case dirUp:
					x--;
					break;
				case dirRight:
					y++;
					break;
				case dirDown:
					x++;
					break;
				case dirLeft:
					y--;
					break;
				}
			}
		}while(!(x == 2 * height - 1 && y == 2 * width - 1));
		maze[x][y] = Path;
		seekList.add(new Box(x, y));
		
		SeekPathAnimation();
	}
	
	//���������յ㣬Ѱ������֮��·����ԭ��ͬѰһ��·��
	public void findAPathByInputPoints(int ax, int ay, int bx, int by){
		if(timeline != null)
			timeline.pause();
		seekList.clear();
		//��ս������
		printInPane();
		
		int x = ax;
		int y = ay;
		Stack<Integer> stackX = new Stack<Integer>();
		Stack<Integer> stackY = new Stack<Integer>();
		do {
			int dir = this.getNextPathDir(x, y);
			if(dir == -1) {
				maze[x][y] = -1;
				//��¼���������������е�·����
				seekList.add(new Box(x, y));
				x = stackX.pop();
				y = stackY.pop();
			}
			else {
				maze[x][y] = Path;
				stackX.add(x);
				stackY.add(y);
				seekList.add(new Box(x, y));
				switch(dir) {
				case dirUp:
					x--;
					break;
				case dirRight:
					y++;
					break;
				case dirDown:
					x++;
					break;
				case dirLeft:
					y--;
					break;
				}
			}
		}while(x <= 2 * height - 1 && y <= 2 * width - 1 && !(x == bx && y == by));
		maze[x][y] = Path;
		seekList.add(new Box(x, y));
		
		SeekPathAnimation();
	}
	
	//·��Ѱ�Ҷ���
	//���ݵ�ǰ���Ƿ�Ϊ���߹�·��Ϊ��һ���߹��ĵ�����·�����·ͼƬ
	protected void SeekPathAnimation() {
		m = 0;
		int dh = (int)600 / (2 * height);
		int dw = (int)600 / (2 * width);
		
		EventHandler<ActionEvent> handler = e -> {
			//Ϊtrue������reΪ·��ͼƬ�����������Ϊδ�߹�·ͼƬ
			boolean isTravel = true;
			//�жϸõ��Ƿ��ڼ�¼������·�����г��ֹ�
			for(int i = m - 1; i >= 0; i--)
				if(seekList.get(m).getX() == seekList.get(i).getX() && 
						seekList.get(m).getY() == seekList.get(i).getY()) {
					isTravel = false;
					break;
				}
			
			ImageView re = null;
			//����õ���ߣ�������Ϊ·��ͼƬ����������Ϊ·
			if(isTravel) {
				re = new ImageView(new Image("file:mazePicture/path.jpg"));
			}
			else {
				re = new ImageView(new Image("file:mazePicture/empty.png"));
				
			}
			//���úõ�һ��λ��ͼƬ��������ͼƬ
			if(m == 1 || m == 0) {
				ImageView start = new ImageView(new Image("file:mazePicture/start.jpg"));
				start.setFitWidth(dw);
				start.setFitHeight(dh);
				add(start, seekList.get(0).getY(), seekList.get(0).getX());
			}
			//���һ��λ����������յ�ͼƬ
			else if(m == seekList.size() - 1){
				re.setFitWidth(dw);
				re.setFitHeight(dh);
				add(re, seekList.get(m - 1).getY(), seekList.get(m - 1).getX());
				
				ImageView q = new ImageView(new Image("file:mazePicture/end.jpg"));
				q.setFitWidth(dw);
				q.setFitHeight(dh);
				add(q, seekList.get(m).getY(), seekList.get(m).getX());
			}
			//һ�����
			else {
				re.setFitWidth(dw);
				re.setFitHeight(dh);
				add(re, seekList.get(m - 1).getY(), seekList.get(m - 1).getX());
				
				//����������ĸõ���һ���޷������ʱ������Ҫ�˻�ʱ
				//�Ѹõ�������Ϊ·��ͼƬ���������߹���Ч��
				if(seekList.get(m - 1).getX() == seekList.get(m + 1).getX() && 
						seekList.get(m - 1).getY() == seekList.get(m + 1).getY()) {
					ImageView tail = new ImageView(new Image("file:mazePicture/path.jpg"));
					tail.setFitWidth(dw);
					tail.setFitHeight(dh);
					add(tail, seekList.get(m).getY(), seekList.get(m).getX());
				}
			}
			
			m++;
		};
		
		timeline = new Timeline(new KeyFrame(Duration.millis(50), handler));
		timeline.setCycleCount(seekList.size());
		timeline.play();
	}
	
	//����-ͨ�������������ԭ�������е��߹�
	public void travelTheMaze() {
		if(timeline != null)
			timeline.pause();
		printInPane();
		
		//ÿ���ĸ�������ж�
		int dx[] = {1, 0, -1, 0};
		int dy[] = {0, 1, 0, -1};
		//�洢ĳһ�㵽���ľ���
		int distance[][] = new int[2 * width + 1][2 * height + 1];
		//���м�¼X,Y���꣬ͨ������ʵ�ֹ������
		Queue<Box> queueXY = new LinkedList<Box>();
		
		//�����������
		queueXY.offer(new Box(1, 1));
		//��������0��0��ľ���Ϊ1���Ժ����μ�¼��ǰ�㵽0,0��ĳ���
		distance[1][1] = 1;
		
		while(queueXY.size() != 0) {
			int x = queueXY.element().getX();
			//��ȡY��ͬʱ������ͷ��Ԫ��ȥ��
			int y = queueXY.poll().getY();
			travelList.add(new Box(x, y));
			int i;
			for(i = 0; i < 4; i++) {
				int nx = x + dx[i];
				int ny = y + dy[i];//�ƶ�������
				
				if(nx >= 0 && nx < 2 * width + 1 && ny >= 0 && ny < 2 * height + 1 && 
						maze[nx][ny] != Wall && distance[nx][ny] == 0) {
					queueXY.offer(new Box(nx, ny));
					//��ǰ��ľ����¼Ϊǰһ������1
					distance[nx][ny] = distance[x][y] + 1;
				}
			}
		}
		//����õ㲻��ǽ���Ҿ������벻��0�� ������Ϊͨ·
		for(int i = 0; i < 2 * width + 1; i++) {
			for(int j = 0; j < 2 * height + 1; j++) {
				if((maze[i][j] != Wall) && distance[i][j] != 0)
					maze[i][j] = Path;
			}
		}
		travelAnimation();
	}
	
	//�������������ݱ������̼�¼�������������ͼƬ���ﵽ����Ч��
	protected void travelAnimation() {
		m = 0;
		int dh = (int)600 / (2 * height);
		int dw = (int)600 / (2 * width);
		EventHandler<ActionEvent> handler = e -> {
			ImageView re = new ImageView(new Image("file:mazePicture/path.jpg"));
			re.setFitWidth(dw);
			re.setFitHeight(dh);
			add(re, travelList.get(m).getY(), travelList.get(m).getX());
			m++;
		};
		
		timeline = new Timeline(new KeyFrame(Duration.millis(20), handler));
		timeline.setCycleCount(travelList.size());
		timeline.play();
	}
	
	//Ѱ�����·��
	/*
	 ���������ڹ�������㷨�����ڱ���ʱͬʱ��¼���·�������⣬�˴�ͨ������һ��·�����飬ÿ������λ��
	�������Ӧ�Թ���ÿ�����꣬·�������е�һ���¼�ߵ��õ��ǰһ������꣬�����ͼ�¼��·��������������
	�������յ�ʱ���ض������·��
	*/
	public void findTheShortestPath() {
		if(timeline != null)
			timeline.pause();
		printInPane();
		
		//ÿ���ĸ�������ж�
		int dx[] = {1, 0, -1, 0};
		int dy[] = {0, 1, 0, -1};
		//·�������¼����
		Box path[][] = new Box[2 * height + 1][2 * width + 1];
		//�洢ĳһ�㵽���ľ���
		int distance[][] = new int[2 * width + 1][2 * height + 1];
		//����X,Y����
		Queue<Box> queueXY = new LinkedList<Box>();
		
		//�����������
		queueXY.offer(new Box(1, 1));
		distance[1][1] = 1;
		
		while(queueXY.size() != 0) {
			int x = queueXY.element().getX();
			//��ȡY��ͬʱ������ͷ��Ԫ��ȥ��
			int y = queueXY.poll().getY();
			int i;
			for(i = 0; i < 4; i++) {
				//�ı��ƶ�������
				int nx = x + dx[i];
				int ny = y + dy[i];
				
				if(nx >= 0 && nx < 2 * width + 1 && ny >= 0 && ny < 2 * height + 1 && 
						maze[nx][ny] != Wall && distance[nx][ny] == 0) {
					queueXY.offer(new Box(nx, ny));
					distance[nx][ny] = distance[x][y] + 1;
					//��¼ǰһ�������
					path[nx][ny] = new Box(x, y);
				if((nx == 2 * width - 1) && (ny == 2 * height - 1))
					//�ﵽ�յ����˳��� ��ʱ��·��һ������̵�
					break;
				}
			}
			if(i != 4)
				break;
		}
		
		path[1][1] = new Box(0, 0);
		int x = 2 * width - 1;
		int y = 2 * height - 1;
		//ѭ�������·������ΪPath��ͨ·
		while((x != 0 && y != 0)) {
			maze[x][y] = Path;
			int mx = path[x][y].getX();
			int my = path[x][y].getY();
			//�õ�ǰһ����������ѭ��
			x = mx;
			y = my;
		}
		//����������·��
		printInPane();
	}
	
	//����Թ�
	public void reset() {
		//��ͣ��ǰ����
		if(timeline != null)
			timeline.pause();
		//��ս�����ӵ�Ԫ��
		this.getChildren().clear();
		//���ĳ�㲻��ǽ��������Ϊ·
		for(int i = 0; i < 2 * height + 1; i++)
			for(int j = 0; j < 2 * width + 1; j++)
				if(maze[i][j] != Wall)
					maze[i][j] = Empty;
		//���÷�������Ϊδ����
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++) {
				mazecell[i][j].setVisited(false);
			}
		printInPane();
	}
	//��ȡ�Թ�
	public int[][] getMaze() {
		return maze;
	}

	//�Թ��޸��������ڴ��ⲿ�޸�
	public void setMaze(int[][] maze) {
		this.maze = maze;
		printInPane();
	}
	
	
}

