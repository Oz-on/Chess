package javaFxSample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;

public class JavaFxSample extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Scene graph
		Group root = new Group();
		
		ObservableList<Node> list = root.getChildren();
		Board chessBoard = new Board(600, 600);
		list.add(chessBoard);
		
		// Scene
		Scene scene = new Scene(root, 600, 600);
		
		// Setting stage configurations
		// Stage is like a window
		primaryStage.setTitle("Chess");
		
		// Adding scene to the stage
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
			chessBoard.setWidth(newVal.floatValue());
			chessBoard.scale();
		});
		primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
			chessBoard.setHeight(newVal.floatValue() - 20);
			chessBoard.scale();
		});
	}
		
	
	public static void main(String args[]) {
		launch(args);
	}
}
