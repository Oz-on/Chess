package javaFxSample;

import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.ImageView;

public class Figure extends ImageView{
	private FileInputStream inputStream;
//	private float boxSize;
//	private float figureSize;
	private String color;
	private int[][] moves; 
	
	public Figure(String filePath, float boxSize, float figureSize, String color) {
//		this.boxSize = boxSize;
//		this.figureSize = figureSize;
		this.color = color;
		
		try {
			inputStream = new FileInputStream(filePath);
			Image image = new Image(inputStream);
			
			this.setImage(image);
			this.setFitWidth(figureSize);
			this.setFitHeight(figureSize);
			
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
	public String getColor() {
		return color;
	}
	public int[][] getMoves() {
		return moves;
	}
}
