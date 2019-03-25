package javaFxSample;

public class Bishop extends Figure{
	private int[][] moves = {
			{-1, -1},
			{1, -1},
			{1, 1},
			{-1, 1}
	};
	public Bishop(String filePath, float boxSize, float figureSize, String direction) {
		super(filePath, boxSize, figureSize, direction);
	}
	@Override
	public int[][] getMoves() {
		return moves;
	}
}
