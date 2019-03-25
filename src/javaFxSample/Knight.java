package javaFxSample;

public class Knight extends Figure {
	private int[][] moves = {
			{1, 2},
			{-1, 2},
			{1, -2},
			{-1, -2},
			{2, 1},
			{-2, 1},
			{2, -1},
			{-2, -1}
	};
	public Knight(String filePath, float boxSize, float figureSize, String direction) {
		super(filePath, boxSize, figureSize, direction);
	}
	@Override
	public int[][] getMoves() {
		return moves;
	}
}
