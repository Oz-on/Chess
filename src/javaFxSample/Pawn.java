package javaFxSample;

public class Pawn extends Figure {
	private int[][] moves = {
			{0, 1},
			{0, 2}
	};
	public Pawn(String imagePath, float boxSize, float pawnSize, String direction) {
		super(imagePath, boxSize, pawnSize, direction);
	}
	@Override
	public int[][] getMoves() {
		return moves;
	}
}
