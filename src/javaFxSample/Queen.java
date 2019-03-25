package javaFxSample;

public class Queen extends Figure{
	private int[][] moves = {
			{-1,-1},
			{0, -1},
			{1, -1},
			{-1, 0},
			{1, 0},
			{-1, 1},
			{0, 1},
			{1, 1}
	};
	public Queen(String filePath, float boxSize, float figureSize, String direction) {
		super(filePath, boxSize, figureSize, direction);
	}
	@Override
	public int[][] getMoves() {
		return moves;
	}
}
