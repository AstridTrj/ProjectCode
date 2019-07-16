package clove;

/*****************************************************************
 * @author 汤荣杰
 * 此类为迷宫功能类，并对相应的功能进行了分开编写
 * 此类中包含了迷宫遍历，寻路，最短路，初始化等功能以及对应的动画
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

//迷宫类
public class MazePane extends GridPane{
	//四个方向常量
	//0-向上  1-向下  2-向左   3-向右
	private final static int dirUp = 0;
	private final static int dirDown = 1;
	private final static int dirLeft = 2;
	private final static int dirRight = 3;
	
	//是否是墙，路，路径的表示
	//0表示未走过的路， 1表示墙， 2表示路径
	private final static int Wall = 1;
	private final static int Empty = 0;
	private final static int Path = 2;
	
	//迷宫宽度
	private int width;
	//迷宫高度
	private int height;
	//迷宫中初始化时的空的单元格数组
	private MazeCell[][] mazecell;
	//迷宫数组
	private int[][] maze;

	
	//存储在遍历或者寻找过程中的坐标，保留路径
	private ArrayList<Box> createList = new ArrayList<Box>();
	private ArrayList<Box> seekList = new ArrayList<Box>();
	private ArrayList<Box> travelList = new ArrayList<Box>();
	//时间轴动画
	private Timeline timeline;
	//路径输出计数
	private int m = 0;
	//运行程序时并行动画的计数,当p=0时才能执行该动画，即初始才需要的动画
	private static int p = 0;
	
	//构造函数，根据给定长度和宽度创建一个迷宫
	public MazePane(int width, int height) {
		this.setHeight(600);
		this.setWidth(600);
		this.width = width;
		this.height = height;
		this.mazecell = new MazeCell[height][width];
		this.maze = new int[2 * height + 1][2 * width + 1];
		
		//循环依次初始化每个单元格
		for(int i = 0; i < this.height; i++)
			for(int j = 0; j < this.width; j++)
				mazecell[i][j] = new MazeCell();
		
		//设置每个坐标格子的大小
		int dw = (int)this.getWidth() / (2 * width);
		int dh = (int)this.getHeight() / (2 * height);
		for(int i = 0; i < 2 * this.height + 1; i++) {
			for(int j = 0; j < 2 * this.width + 1; j++){
				//先设置为全部是墙，即运行时全部展示为墙
				ImageView re = new ImageView(new Image("file:mazePicture/wall.jpg"));
				re.setFitWidth(dw);
				re.setFitHeight(dh);
				//添加到当前面板
				add(re, j, i);
			}
		}
		//淡入淡出动画，定义一个动画，设置其值从0变到1，循环周期为1次
		FadeTransition ft = new FadeTransition(Duration.millis(4200), this);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.setCycleCount(1);
		ft.play();
		
		//如果p=0就执行初始运行的动画，否则不运行
		if(p == 0)
			animation();
		p++;
	}
	
	//并行动画
	protected void animation() {
		//淡入淡出动画
		FadeTransition ft = new FadeTransition(Duration.millis(5000), this);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.setCycleCount(1);
		
		//旋转动画，旋转角度为720度，周期一次
		RotateTransition ro = new RotateTransition(Duration.millis(3000), this);
		ro.setByAngle(720);
		ro.setCycleCount(1);
		
		//缩放动画
		ScaleTransition sc = new ScaleTransition(Duration.millis(4000), this);
		sc.setFromX(0);
		sc.setFromY(0);
		sc.setToX(1);
		sc.setToY(1);
		sc.setCycleCount(1);
		
		//并行动画执行，将淡入淡出，旋转，缩放动画并行执行
		ParallelTransition pa = new ParallelTransition(ft, ro, sc);
		pa.setCycleCount(1);
		pa.play();
	}
	
	//判断dir方向是否有未被访问的单元格
	protected boolean ifAroundHasNotVisited(int x, int y, int dir){
		boolean isAroundVisited = false;
		switch(dir){
		case dirUp:
			//是否在边界
			if(x <= 0)
				isAroundVisited = true;
			//若不是边界，则返回该方向单元格的Visited属性
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
	
	//重载方法ifAroundHasNotVisited（）
	//如果周围至少有一个单元格未被访问，则返回true，表示可继续搜索
	protected boolean ifAroundHasNotVisited(int x, int y){
		return (this.ifAroundHasNotVisited(x, y, dirUp) || this.ifAroundHasNotVisited(x, y, dirRight)
				|| this.ifAroundHasNotVisited(x, y, dirDown) || this.ifAroundHasNotVisited(x, y, dirLeft));
	}
	
	//随机产生 一个方向，如果该方向全部被访问则重新产生
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
	
	//根据dir通过修改迷宫单元格的属性去掉可走路径之间的墙，以形成迷宫效果
	protected void pushWall(int x, int y, int dir){
		switch(dir){
		case dirUp:
			//去掉其上方墙
			mazecell[x][y].setUp(false);
			//去掉其下方墙
			mazecell[x - 1][y].setDown(false);
			//记录该点及其另一可走点
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
	
	//通过深度优先搜索原理遍历网格，生成迷宫
	public void travelAllCell(){
		//从（0，0）开始
		int x = 0;
		int y = 0;
		//X坐标栈，存储坐标X坐标
		Stack<Integer> stackX = new Stack<Integer>();
		//Y坐标栈，存储坐标Y坐标
		Stack<Integer> stackY = new Stack<Integer>();
		
		do{
			MazeCell p = mazecell[x][y];
			//是否被访问，未访问则设置为已被访问
			if( !p.isVisited() ){
				p.setVisited(true);
			}
			
			//如果该点周围有未被访问的点则执行if下的语句
			if(ifAroundHasNotVisited(x, y)){
				//获取产生的方向
				int dir = this.getRandomDir(x, y);
				//去掉可走单元格之间的墙
				this.pushWall(x, y, dir);
				//将当前点装入栈，并以该点继续循环
				stackX.add(x);
				stackY.add(y);
				
				//根据方向，将此方向的点作为当前点，即坐标变化
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
				//记录生成过程中的路径，即点坐标的先后顺序，用于生成动画
				createList.add(new Box(2 * x + 1, 2 * y + 1));
				//如果改方向的已经全部被访问则弹出当前点坐标，向后退
				x = stackX.pop();
				y = stackY.pop();
			}
		}while( !stackX.isEmpty());
	}
	
	//遍历之后，根据属性，将每个迷宫坐标设置为对应的墙，路或者路径
	public void createMaze(){
		this.travelAllCell();
		for(int j = 0; j < 2 * width + 1; j++)
			maze[0][j] = Wall;
		for(int i = 0; i < height; i++){
			maze[2 * i + 1][0] = Wall;
			for(int j = 0; j < width; j++){
				maze[2 * i + 1][2 * j + 1] = Empty;
				//其右方的墙是否可去掉
				if(mazecell[i][j].isRight())
					maze[2 * i + 1][2 * j + 2] = Wall;
				else
					maze[2 * i + 1][2 * j + 2] = Empty;
			}
			//由于生成是从左到右，从上到下，故不判断左和上方的墙
			maze[2 * i + 2][0] = Wall;
			for(int j = 0; j < width; j++){
				//其下方的墙是否可去掉
				if(mazecell[i][j].isDown())
					maze[2 * i + 2][2 * j + 1] = Wall;
				else
					maze[2 * i + 2][2 * j + 1] = Empty;
				maze[2 * i + 2][2 * j + 2] = Wall;
			}
		}
		
		//横向-设置更多的空，以达到多条路径
		//原理：判断是否接连4个都是墙，若是，则挖出一个洞，且每行只挖一个
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
		//调用生成动画
		generationAnimation();
	}
	
	//在面板输出，为wall输出wall图片，empty和path同理
	public void printInPane() {
		int dh = (int)600 / (2 * height);
		int dw = (int)600 / (2 * width);
		for(int i = 0; i < 2 * height + 1; i++) {
			for(int j = 0; j < 2 * width + 1; j++) {
				//如果该坐标的属性是墙，则输出墙的图片
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
	
	//迷宫生成动画，根据记录的路径依次输出，以达到动画效果
	protected void generationAnimation() {
		int dh = (int)600 / (2 * height);
		int dw = (int)600 / (2 * width);
		EventHandler<ActionEvent> handler = e -> {
			ImageView re = new ImageView(new Image("file:mazePicture/empty.png"));
			re.setFitWidth(dw);
			re.setFitHeight(dh);
			add(re, createList.get(m).getY(), createList.get(m).getX());
			
			//添加一个淡入淡出动画
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
	
	//寻找路径时get下一个路径方向，如果该点的下一个方向未走，就记录当前方向并返回
	//当下次寻路时，就按照返回的方向寻路
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
	
	//寻找一条路径，通过深度优先搜索的原理搜素，查找出一条路径
	public void findAPath(){
		//如果当前面板还有其他正在执行动画，则先停止该动画
		if(timeline != null)
			timeline.pause();
		seekList.clear();
		//清空界面输出
		printInPane();
		
		int x = 1;
		int y = 1;
		Stack<Integer> stackX = new Stack<Integer>();
		Stack<Integer> stackY = new Stack<Integer>();
		do {
			int dir = this.getNextPathDir(x, y);
			if(dir == -1) {
				maze[x][y] = -1;
				//记录下搜索过程中所有的路径点
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
	
	//输入起点和终点，寻找两点之间路径，原理同寻一条路径
	public void findAPathByInputPoints(int ax, int ay, int bx, int by){
		if(timeline != null)
			timeline.pause();
		seekList.clear();
		//清空界面输出
		printInPane();
		
		int x = ax;
		int y = ay;
		Stack<Integer> stackX = new Stack<Integer>();
		Stack<Integer> stackY = new Stack<Integer>();
		do {
			int dir = this.getNextPathDir(x, y);
			if(dir == -1) {
				maze[x][y] = -1;
				//记录下搜索过程中所有的路径点
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
	
	//路径寻找动画
	//根据当前点是否为已走过路径为上一步走过的点设置路径或空路图片
	protected void SeekPathAnimation() {
		m = 0;
		int dh = (int)600 / (2 * height);
		int dw = (int)600 / (2 * width);
		
		EventHandler<ActionEvent> handler = e -> {
			//为true即设置re为路径图片，否则就设置为未走过路图片
			boolean isTravel = true;
			//判断该点是否在记录的搜索路径点中出现过
			for(int i = m - 1; i >= 0; i--)
				if(seekList.get(m).getX() == seekList.get(i).getX() && 
						seekList.get(m).getY() == seekList.get(i).getY()) {
					isTravel = false;
					break;
				}
			
			ImageView re = null;
			//如果该点可走，则设置为路径图片，否则设置为路
			if(isTravel) {
				re = new ImageView(new Image("file:mazePicture/path.jpg"));
			}
			else {
				re = new ImageView(new Image("file:mazePicture/empty.png"));
				
			}
			//设置好第一个位置图片，即起点的图片
			if(m == 1 || m == 0) {
				ImageView start = new ImageView(new Image("file:mazePicture/start.jpg"));
				start.setFitWidth(dw);
				start.setFitHeight(dh);
				add(start, seekList.get(0).getY(), seekList.get(0).getX());
			}
			//最后一个位置情况，即终点图片
			else if(m == seekList.size() - 1){
				re.setFitWidth(dw);
				re.setFitHeight(dh);
				add(re, seekList.get(m - 1).getY(), seekList.get(m - 1).getX());
				
				ImageView q = new ImageView(new Image("file:mazePicture/end.jpg"));
				q.setFitWidth(dw);
				q.setFitHeight(dh);
				add(q, seekList.get(m).getY(), seekList.get(m).getX());
			}
			//一般情况
			else {
				re.setFitWidth(dw);
				re.setFitHeight(dh);
				add(re, seekList.get(m - 1).getY(), seekList.get(m - 1).getX());
				
				//如果搜索到的该点下一次无方向可走时，即需要退回时
				//把该点先设置为路径图片，以体现走过的效果
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
	
	//遍历-通过广度优先搜索原理，将所有点走过
	public void travelTheMaze() {
		if(timeline != null)
			timeline.pause();
		printInPane();
		
		//每次四个方向的判断
		int dx[] = {1, 0, -1, 0};
		int dy[] = {0, 1, 0, -1};
		//存储某一点到起点的距离
		int distance[][] = new int[2 * width + 1][2 * height + 1];
		//队列记录X,Y坐标，通过队列实现广度搜素
		Queue<Box> queueXY = new LinkedList<Box>();
		
		//将起点加入队列
		queueXY.offer(new Box(1, 1));
		//设置起点距0，0点的距离为1，以后依次记录当前点到0,0点的长度
		distance[1][1] = 1;
		
		while(queueXY.size() != 0) {
			int x = queueXY.element().getX();
			//获取Y的同时将队列头的元素去掉
			int y = queueXY.poll().getY();
			travelList.add(new Box(x, y));
			int i;
			for(i = 0; i < 4; i++) {
				int nx = x + dx[i];
				int ny = y + dy[i];//移动后坐标
				
				if(nx >= 0 && nx < 2 * width + 1 && ny >= 0 && ny < 2 * height + 1 && 
						maze[nx][ny] != Wall && distance[nx][ny] == 0) {
					queueXY.offer(new Box(nx, ny));
					//当前点的距离记录为前一点距离加1
					distance[nx][ny] = distance[x][y] + 1;
				}
			}
		}
		//如果该点不是墙，且距起点距离不是0， 则设置为通路
		for(int i = 0; i < 2 * width + 1; i++) {
			for(int j = 0; j < 2 * height + 1; j++) {
				if((maze[i][j] != Wall) && distance[i][j] != 0)
					maze[i][j] = Path;
			}
		}
		travelAnimation();
	}
	
	//遍历动画，根据遍历过程记录的坐标输出依次图片，达到动画效果
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
	
	//寻找最短路径
	/*
	 方法：由于广度搜索算法不好在遍历时同时记录最短路径的问题，此处通过创建一个路径数组，每个数组位置
	的坐标对应迷宫的每个坐标，路径数组中的一点记录走到该点的前一点的坐标，这样就记录了路径，当搜索优先
	搜索到终点时，必定是最短路径
	*/
	public void findTheShortestPath() {
		if(timeline != null)
			timeline.pause();
		printInPane();
		
		//每次四个方向的判断
		int dx[] = {1, 0, -1, 0};
		int dy[] = {0, 1, 0, -1};
		//路径坐标记录数组
		Box path[][] = new Box[2 * height + 1][2 * width + 1];
		//存储某一点到起点的距离
		int distance[][] = new int[2 * width + 1][2 * height + 1];
		//队列X,Y坐标
		Queue<Box> queueXY = new LinkedList<Box>();
		
		//将起点加入队列
		queueXY.offer(new Box(1, 1));
		distance[1][1] = 1;
		
		while(queueXY.size() != 0) {
			int x = queueXY.element().getX();
			//获取Y的同时将队列头的元素去掉
			int y = queueXY.poll().getY();
			int i;
			for(i = 0; i < 4; i++) {
				//改变移动后坐标
				int nx = x + dx[i];
				int ny = y + dy[i];
				
				if(nx >= 0 && nx < 2 * width + 1 && ny >= 0 && ny < 2 * height + 1 && 
						maze[nx][ny] != Wall && distance[nx][ny] == 0) {
					queueXY.offer(new Box(nx, ny));
					distance[nx][ny] = distance[x][y] + 1;
					//记录前一点的坐标
					path[nx][ny] = new Box(x, y);
				if((nx == 2 * width - 1) && (ny == 2 * height - 1))
					//达到终点则退出， 此时的路径一定是最短的
					break;
				}
			}
			if(i != 4)
				break;
		}
		
		path[1][1] = new Box(0, 0);
		int x = 2 * width - 1;
		int y = 2 * height - 1;
		//循环将最短路径设置为Path即通路
		while((x != 0 && y != 0)) {
			maze[x][y] = Path;
			int mx = path[x][y].getX();
			int my = path[x][y].getY();
			//得到前一点的坐标继续循环
			x = mx;
			y = my;
		}
		//调用输出最短路径
		printInPane();
	}
	
	//清空迷宫
	public void reset() {
		//暂停当前动画
		if(timeline != null)
			timeline.pause();
		//清空界面添加的元素
		this.getChildren().clear();
		//如果某点不是墙，则设置为路
		for(int i = 0; i < 2 * height + 1; i++)
			for(int j = 0; j < 2 * width + 1; j++)
				if(maze[i][j] != Wall)
					maze[i][j] = Empty;
		//设置访问属性为未访问
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++) {
				mazecell[i][j].setVisited(false);
			}
		printInPane();
	}
	//获取迷宫
	public int[][] getMaze() {
		return maze;
	}

	//迷宫修改器，便于从外部修改
	public void setMaze(int[][] maze) {
		this.maze = maze;
		printInPane();
	}
	
	
}

