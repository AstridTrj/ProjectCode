package clove;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TangRongjiec extends Application{
	
	private MazePane maze = null;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane pane = new BorderPane();
		
		maze = new MazePane(10, 10);
		maze.setPadding(new Insets(10, 10, 10, 10));
		
		//功能按钮
		Button btGenerate = new Button("Generate a maze\n(default: 27 * 27)");
		Button btManual = new Button("Txt Maze");
		Button btSeekAWay = new Button("Seek A Way");
		Button btTravelTheMaze = new Button("Travel The Maze");
		Button btFindTheShortestWay = new Button("Seek The Shortest Way");
		Button btInputPoints = new Button("Seek A Way By Points");
		Button btChoose = new Button("Choose The Size:");
		Button btReset = new Button("Reset");
		RadioButton size1 = new RadioButton("21 X 21");
		RadioButton size2 = new RadioButton("51 X 51");
		RadioButton size3 = new RadioButton("71 X 71");
		
		//设置功能按钮的属性，字体类型，大小等
		btGenerate.setFont(Font.font("Times New Roman",FontWeight.BOLD, FontPosture.ITALIC, 15));
		btManual.setFont(Font.font("Times New Roman",FontWeight.BOLD, FontPosture.ITALIC, 15));
		btSeekAWay.setFont(Font.font("Times New Roman",FontWeight.BOLD, FontPosture.ITALIC, 15));
		btTravelTheMaze.setFont(Font.font("Times New Roman",FontWeight.BOLD, FontPosture.ITALIC, 15));
		btFindTheShortestWay.setFont(Font.font("Times New Roman",FontWeight.BOLD, FontPosture.ITALIC, 15));
		btChoose.setFont(Font.font("Times New Roman",FontWeight.BOLD, FontPosture.ITALIC, 15));
		btInputPoints.setFont(Font.font("Times New Roman",FontWeight.BOLD, FontPosture.ITALIC, 15));
		btReset.setFont(Font.font("Times New Roman",FontWeight.BOLD, FontPosture.ITALIC, 15));
		size1.setFont(Font.font("Times New Roman",FontWeight.BOLD, FontPosture.ITALIC, 15));
		size2.setFont(Font.font("Times New Roman",FontWeight.BOLD, FontPosture.ITALIC, 15));
		size3.setFont(Font.font("Times New Roman",FontWeight.BOLD, FontPosture.ITALIC, 15));
		
		//设置按钮背景颜色
		btGenerate.setStyle("-fx-base: skyblue;");
		btManual.setStyle("-fx-base: skyblue;");
		btSeekAWay.setStyle("-fx-base: skyblue;");
		btTravelTheMaze.setStyle("-fx-base: skyblue;");
		btFindTheShortestWay.setStyle("-fx-base: skyblue;");
		btReset.setStyle("-fx-base: skyblue;");
		btInputPoints.setStyle("-fx-base: skyblue;");
		btChoose.setStyle("-fx-base: skyblue;");
		
		//设置大小按钮为一个组
		ToggleGroup group = new ToggleGroup();
		size1.setToggleGroup(group);
		size2.setToggleGroup(group);
		size3.setToggleGroup(group);
		
		//Maze的效果
		Text text = new Text("Maze");
		text.setFont(Font.font("Times New Roman",FontWeight.BOLD, FontPosture.ITALIC, 90));
		Text tblank = new Text("");
		//将按钮添加到VBox
		VBox sizeVbox = new VBox(5);
		sizeVbox.setAlignment(Pos.CENTER);
		sizeVbox.getChildren().addAll(size1, size2, size3);
		btChoose.setDisable(false);
		
		VBox bt = new VBox(10);
		bt.setPadding(new Insets(0, 30, 0, 0));
		bt.setAlignment(Pos.CENTER);
		BorderPane.setAlignment(bt, Pos.CENTER);
		//将按钮添加到VBox
		bt.getChildren().addAll(text, tblank, btGenerate, btManual, btInputPoints, btSeekAWay, btTravelTheMaze, 
				btFindTheShortestWay, btReset, btChoose, sizeVbox);
		//设置面板背景图片
		ImageView background = new ImageView(new Image("file:background.jpg"));
		
		pane.getChildren().add(background);
		pane.setRight(bt);
		pane.setCenter(maze);
		
		
		
		btGenerate.setOnAction(e -> {
			if(maze != null)
				pane.getChildren().remove(maze);
			maze = new MazePane(13, 13);
			
			maze.createMaze();
			maze.setPadding(new Insets(10, 10, 10, 10));
			pane.setCenter(maze);
			size1.setSelected(false);
			size2.setSelected(false);
			size3.setSelected(false);
		});
		
		//迷宫导入按钮
		btManual.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(primaryStage);
            if(file!=null)
				try (Scanner in = new Scanner(file)) {
					if(maze != null)
					pane.getChildren().remove(maze);
					maze = new MazePane(15, 15);
					
					int i = 0, j = 0;
		            int[][] txt = new int[31][31];
		            while(in.hasNext()) {
		            	txt[i][j++] = in.nextInt();
		            	if(j == txt[0].length) {
		            		i++;
		            		j = 0;
		            	}
		            	
		            }
		            
		            maze.setMaze(txt);
		            maze.setPadding(new Insets(10, 10, 10, 10));
		            pane.setCenter(maze);
				}
				catch (FileNotFoundException e1) {
					e1.printStackTrace();
	            }
			
			
		});
		
		btSeekAWay.setOnAction(e -> {
			maze.reset();
			maze.findAPath();
			
		});
		
		btTravelTheMaze.setOnAction(e -> {
			maze.reset();
			maze.travelTheMaze();
		});
		
		btFindTheShortestWay.setOnAction(e -> {
			maze.reset();
			maze.findTheShortestPath();
		});
		
		btReset.setOnAction(e -> {
			maze.reset();
			maze.printInPane();
		});
		
		size1.setOnAction(e -> {
			if(size1.isSelected()) {
				if(maze != null)
					pane.getChildren().remove(maze);
				maze = new MazePane(10, 10);
				
				maze.createMaze();
				maze.setPadding(new Insets(10, 10, 10, 10));
				pane.setCenter(maze);
			}
		});
		
		size2.setOnAction(e -> {
			if(size2.isSelected()) {
				if(maze != null)
					pane.getChildren().remove(maze);
				maze = new MazePane(25, 25);
				
				maze.createMaze();
				maze.setPadding(new Insets(10, 10, 10, 10));
				pane.setCenter(maze);
			}
		});
		
		size3.setOnAction(e -> {
			if(size3.isSelected()) {
				if(maze != null)
					pane.getChildren().remove(maze);
				maze = new MazePane(35, 35);
				
				maze.createMaze();
				maze.setPadding(new Insets(10, 10, 10, 10));
				pane.setCenter(maze);
			}
		});
		
		//输入起点和终点
		btInputPoints.setOnAction(e -> {
			boolean flag = false;
			while(!flag) {
				int[] pn = inputThePoints();
				if(pn != null) {
					//不满足条件则给予警告
					if(maze.getMaze()[pn[0]][pn[1]] == 1 || maze.getMaze()[pn[2]][pn[3]] == 1) {
						Alert error = new Alert(AlertType.WARNING);
						error.setHeaderText("ERROR");
						error.setContentText("The inputs should be two empty path");
						error.showAndWait();
						if(error.getAlertType() != AlertType.WARNING)
							flag = true;
						flag = false;
					}
					else {
						maze.reset();
						maze.findAPathByInputPoints(pn[0], pn[1], pn[2], pn[3]);
						flag = true;
					}
				}
				else
					break;
			}
			
		});

		Scene scene = new Scene(pane, 900, 650);
		background.setFitWidth(scene.getWidth());
		background.setFitHeight(scene.getHeight());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Game Of Maze");
		primaryStage.show();
	}
	
	private int[] inputThePoints() {
		int[] points = new int[4];
		//对话框内容
		VBox vP = new VBox();
		TextField pointOneX = new TextField();
		TextField pointOneY = new TextField();
		TextField pointTwoX = new TextField();
		TextField pointTwoY = new TextField();
		pointOneX.setPromptText("point one's x");
		pointOneY.setPromptText("point one's y");
		pointTwoX.setPromptText("point two's x");
		pointTwoY.setPromptText("point two's y");
		vP.setSpacing(10);
		vP.getChildren().addAll(pointOneX, pointOneY, pointTwoX, pointTwoY);
		
		//Dialog -> DialogPane -> Root Node
		DialogPane dialogPane = new DialogPane();
		dialogPane.setContent(vP);
		
		//添加确定按钮
		ButtonType ok = new ButtonType(" OK ", ButtonData.OK_DONE);
		dialogPane.getButtonTypes().add(ok);
		
		//创建对话框
		Dialog<ButtonType> dig = new Dialog<ButtonType>();
		dig.setDialogPane(dialogPane);
		dig.setTitle("Input two points");
		
		//显示对话框并接受返回结果
		Optional<ButtonType> result = dig.showAndWait();
		if(result.isPresent() && result.get().getButtonData() == ButtonData.OK_DONE) {
			if((pointOneX.getText().length() != 0) && (pointOneY.getText().length() != 0) &&
					(pointTwoX.getText().length() != 0) && (pointTwoY.getText().length() != 0)) {
				points[0] = Integer.valueOf(pointOneX.getText());
				points[1] = Integer.valueOf(pointOneY.getText());
				points[2] = Integer.valueOf(pointTwoX.getText());
				points[3] = Integer.valueOf(pointTwoY.getText());
			}
		}
		else
			return null;
		return points;
	}
	
}




