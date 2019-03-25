package javaFxSample;

public class Rook extends Figure {
	private int[][] moves = {
			{0, -1},
			{1, 0},
			{0, 1},
			{-1, 0}
	};
	public Rook(String imagePath, float boxSize, float figureSize, String direction) {
		super(imagePath, boxSize, figureSize, direction);
	}
	@Override
	public int[][] getMoves() {
		return moves;
	}
}
