package javaFxSample;

import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board extends Group {
	// window width
	public float width;
	// window height
	public float height;
	public float boardSize = 600;
	// Size of the one box that are part of the board
	public float boxSize = boardSize / 8;
	// variables that are responsible for scaling the board
	public float xToCenter = 0;
	public float yToCenter = 0;
	
	public float figureSize = 75;
	// Array that represents placement of the figures
	private Figure[][] board = new Figure[8][8];
	private Rectangle[][] rectBoard = new Rectangle[8][8];
	private int[] selectedFigurePosition = {-1, -1};
	// Arrays that contain all possible moves for selected figure
	private List<int[]> avaiableMoves = new ArrayList<int[]>();
	private List<int[]> possibleBeatFieldBlack = new ArrayList<int[]>();
	private List<int[]> possibleBeatFieldWhite = new ArrayList<int[]>();
//	private List<int[]> possibleBeatField = new ArrayList<int[]>();

	// Variable indicating for whom move belong
	private int move = 0;
	
	private boolean isCheck = false;
	
	
	public Board(float initialWidth, float initialHeight) {
		width = initialWidth;
		height = initialHeight;
		initializeRectBoard();
		initializeFigureBoard();
		drawChessBoard();
		
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						if (board[i][j] != null) {
							// If figure was clicked and turn belongs to figure of that color
							if (board[i][j].equals(event.getTarget()) && board[i][j].getColor() == (move == 0 ? "white" : "black")) {
								clearMoves();
								unmarkPossibleMovesOnBoard();
								
								//Select or deselect the figure
								int xPosition = selectedFigurePosition[1];
								int yPosition = selectedFigurePosition[0];
								selectedFigurePosition[0] = ((xPosition != -1 && yPosition != -1) && board[yPosition][xPosition].equals(event.getTarget())) ? -1 : i;
								selectedFigurePosition[1] = ((xPosition != -1 && yPosition != -1) && board[yPosition][xPosition].equals(event.getTarget())) ? -1 : j;
								
								//If the figure is selected
								if (selectedFigurePosition[0] != -1 && selectedFigurePosition[1] != -1) {
									checkPossibleMoves(selectedFigurePosition[0], selectedFigurePosition[1]);
									// If check occurs, make sure that selected figure can defend that check
									canDefendCheck(selectedFigurePosition[0], selectedFigurePosition[1], move == 0 ? 1 : 0);
									markPossibleMovesOnBoard();
								}
							}
						}
					}
				}
				// If any figure was selected and can has places to move
				if (selectedFigurePosition[0] != -1 && selectedFigurePosition[1] != -1 && avaiableMoves.size() > 0) {
					// Take available moves and check for every move whether target is equal with it.
					for (int i = 0; i < avaiableMoves.size(); i++) {
						int xPosition = avaiableMoves.get(i)[1];
						int yPosition = avaiableMoves.get(i)[0];
						// If selected place is equal place where selected figure can move
						if(rectBoard[yPosition][xPosition].equals(event.getTarget()) || (board[yPosition][xPosition] != null && board[yPosition][xPosition].equals(event.getTarget()))) {
							moveFigure(xPosition, yPosition);
							unmarkPossibleMovesOnBoard();
						}
					}
				}
			}
		};
		
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	private void drawChessBoard()  {
		this.getChildren().clear();
		//Add rectangles to the board
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				this.getChildren().add(rectBoard[i][j]);
			}
		}
		//Add figures to the board
		addFigures();
	}
	private void initializeRectBoard() {
		// this method create rectangle object instances and adds them to rectBoard
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				rectBoard[i][j] = new Rectangle(j * boxSize, i * boxSize, boxSize, boxSize);
				if ((j + i) % 2 == 0) {
					rectBoard[i][j].setFill(Color.ANTIQUEWHITE);
				} else {
					rectBoard[i][j].setFill(Color.BROWN);
				}
			}
		}
	}
	private void initializeFigureBoard() {
		// Add figures to board
		board[0][0] = new Rook("assets/rook.png", boxSize, figureSize, "white");
		board[0][1] = new Knight("assets/knight.png", boxSize, figureSize, "white");
		board[0][2] = new Bishop("assets/bishop.png", boxSize, figureSize, "white");
		board[0][3] = new King("assets/king.png", boxSize, figureSize, "white");
		board[0][4] = new Queen("assets/queen.png", boxSize, figureSize, "white");
		board[0][5] = new Bishop("assets/bishop.png", boxSize, figureSize, "white");
		board[0][6] = new Knight("assets/knight.png", boxSize, figureSize, "white");
		board[0][7] = new Rook("assets/rook.png", boxSize, figureSize, "white");
		
		board[7][0] = new Rook("assets/rook_black.png", boxSize, figureSize, "black");
		board[7][1] = new Knight("assets/knight_black.png", boxSize, figureSize, "black");
		board[7][2] = new Bishop("assets/bishop_black.png", boxSize, figureSize, "black");
		board[7][3] = new King("assets/king_black.png", boxSize, figureSize, "black");
		board[7][4] = new Queen("assets/queen_black.png", boxSize, figureSize, "black");
		board[7][5] = new Bishop("assets/bishop_black.png", boxSize, figureSize, "black");
		board[7][6] = new Knight("assets/knight_black.png", boxSize, figureSize, "black");
		board[7][7] = new Rook("assets/rook_black.png", boxSize, figureSize, "black");
		
		//Add pawns
		for (int i = 0; i < 8; i++) {
			board[1][i] = new Pawn("assets/pawn.png", boxSize, figureSize, "white");
			board[6][i] = new Pawn("assets/pawn_black.png", boxSize, figureSize, "black");
		}
	}
	private void addFigures() {
		// This function add figures to node
		for (int i = 0; i < 8; i++) {
			//Add white figures
			addFigure(boxSize * i + xToCenter, 0, board[0][i]);
			addFigure(boxSize * i + xToCenter, boxSize, board[1][i]);
			
			//Add black figures
			addFigure(boxSize * i + xToCenter, boxSize * 6, board[6][i]);
			addFigure(boxSize * i + yToCenter, boxSize * 7, board[7][i]);
		}
	}
	private void addFigure(float xPos, float yPos, Figure figure) {
		figure.setX(xPos);
		figure.setY(yPos);
		this.getChildren().add(figure);
	}
	public void scale() {
		if(width < height) {
			boardSize = width;
			xToCenter = 0;
			yToCenter = (height - boardSize) / 2;
		} else {
			boardSize = height;
			xToCenter = (width - boardSize) / 2;
			yToCenter = 0;
		}
		boxSize = boardSize / 8;
		
		for (int i = 0; i < 8; i++) {
			float yPosition = boxSize * i + yToCenter;
			for (int j = 0; j < 8; j++) {
				float xPosition = boxSize * j + xToCenter;
				rectBoard[i][j].setX(xPosition);
				rectBoard[i][j].setY(yPosition);
				rectBoard[i][j].setWidth(boxSize);
				rectBoard[i][j].setHeight(boxSize);
				
				if(board[i][j] != null) {
					board[i][j].setX(xPosition);
					board[i][j].setY(yPosition);
					board[i][j].setFitWidth(boxSize);
					board[i][j].setFitHeight(boxSize);
				}
			}
		}
	}
	public void checkPossibleMoves(int selectedFigurePosY, int selectedFigurePosX) {
		Figure selectedFigure = board[selectedFigurePosY][selectedFigurePosX];
		int[][] figureMoves = selectedFigure.getMoves();
		String figureColor = selectedFigure.getColor();
		
		//If selected figure is a pawn (it has specific moves)
		if(selectedFigure.getClass() == Pawn.class) {
			//If pawn's position is equal to initial position, check two possible moves, otherwise check only first one
			int moves = selectedFigurePosY == 1 || selectedFigurePosY == 6 ? figureMoves[1][1] : figureMoves[0][1];
			int i = 0;
			int j = 0;
			int x = selectedFigurePosX;
			int y = selectedFigurePosY;
			
			while (i < moves) {
				y += (figureColor == "black" ? -1 : 1);
				// If something stands on the way, stop iterating
				if(board[y][x] != null) {
					break;
				}
				//otherwise add position to the available moves
				avaiableMoves.add(j, new int[] {y, x});
				j++;
				i++;
			}
			int nextPosY = selectedFigurePosY + (figureColor == "black" ? -1 : 1);
			// If something stands on the place where pawn can beat, add this place to possible move
			if ((x-1) >= 0 && board[nextPosY][x - 1] != null && board[nextPosY][x - 1].getColor() != figureColor) {
				int[] tablePosition = {nextPosY, x - 1};
				avaiableMoves.add(j, tablePosition);
				j++;
			}
			if ((x+1) < 8 && board[nextPosY][x + 1] != null && board[nextPosY][x + 1].getColor() != figureColor) {
				int[] tablePosition = {nextPosY, x + 1};
				avaiableMoves.add(j, tablePosition);
				j++;
			}
		} else if (selectedFigure.getClass() == Knight.class || selectedFigure.getClass() == King.class) {
			// If selected figure is a knight or king
			int j = 0;
			for (int i = 0; i < figureMoves.length; i++) {
				int x = selectedFigurePosX + figureMoves[i][0];
				int y = selectedFigurePosY+ figureMoves[i][1];
				if (x >= 0 && x < 8 && y >= 0 && y < 8 && (board[y][x] == null || board[y][x].getColor() != figureColor)) {
					int[] tablePosition = {y, x};
					avaiableMoves.add(j, tablePosition);
					j++;
				}
			}
		} else {
			// Jeśli zaznaczona figura to wieża, hetman lub goniec
			int j = 0;
			for (int i = 0; i < figureMoves.length; i++) {
				//Weź wektor przesunięcia
				int xMove = figureMoves[i][0];
				int yMove = figureMoves[i][1];
				int x = selectedFigurePosX + xMove;
				int y = selectedFigurePosY + yMove;
				//Dopóki nie napotka przeszkody w postaci figury tego samego koloru, wykonuj w pętli
				while(x >= 0 && x < 8 && y >= 0 && y < 8 && board[y][x] == null) {
					avaiableMoves.add(j, new int[] {y, x});
					j++;
					x += xMove;
					y += yMove;
				}
				// Jeśli pierwsza napotkana figura jest figurą przeciwnika, dodaj ją do możliwych ruchów
				if (x >= 0 && x < 8 && y >= 0 && y < 8 && board[y][x] != null && board[y][x].getColor() != selectedFigure.getColor()) {
					avaiableMoves.add(j, new int[]{y, x});
				}
			}
		}
	}
	private void checkPossibleBeatField(Figure[][] figureBoard) {
		// Ta metoda sprawdza dla każdej figury, jej możliwość do zbicia
		// i przechowuje pola, na których figury danego koloru mogą zbijać
		
		//At first clear previous data
		possibleBeatFieldBlack.removeAll(possibleBeatFieldBlack);
		possibleBeatFieldWhite.removeAll(possibleBeatFieldWhite);
		
		int beatIteratorWhite = 0, beatIteratorBlack = 0;
		// For every figure on board
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (figureBoard[i][j] != null) {
					Figure selectedFigure = figureBoard[i][j];
					int[][] figureMoves = selectedFigure.getMoves();
					String figureColor = selectedFigure.getColor();
					
					if(selectedFigure.getClass() == Pawn.class) {
						int x = j;
						int y = i + (figureColor == "black" ? -1 : 1);
						
						if ((x-1) >= 0 && figureBoard[y][x - 1] != null && figureBoard[y][x - 1].getColor() != figureColor) {
							int[] tablePosition = {y, x - 1};
							if (figureColor == "black") {
								possibleBeatFieldBlack.add(beatIteratorBlack, tablePosition);
								beatIteratorBlack++;
							} else {
								possibleBeatFieldWhite.add(beatIteratorWhite, tablePosition);
								beatIteratorWhite++;
							}
						}
						if ((x+1) < 8 && figureBoard[y][x + 1] != null && figureBoard[y][x + 1].getColor() != figureColor) {
							int[] tablePosition = {y, x + 1};
							if (figureColor == "black") {
								possibleBeatFieldBlack.add(beatIteratorBlack, tablePosition);
								beatIteratorBlack++;
							} else {
								possibleBeatFieldWhite.add(beatIteratorWhite, tablePosition);
								beatIteratorWhite++;
							}
						}
					} else if (selectedFigure.getClass() == Knight.class || selectedFigure.getClass() == King.class) {
						for (int k = 0; k < figureMoves.length; k++) {
							int x = j + figureMoves[k][0];
							int y = i + figureMoves[k][1];
							if (x >= 0 && x < 8 && y >= 0 && y < 8 && (figureBoard[y][x] == null || figureBoard[y][x].getColor() != figureColor)) {
								int[] tablePosition = {y, x};
								if (figureColor == "black") {
									possibleBeatFieldBlack.add(beatIteratorBlack, tablePosition);
									beatIteratorBlack++;
								} else {
									possibleBeatFieldWhite.add(beatIteratorWhite, tablePosition);
									beatIteratorWhite++;
								}
							}
						}
					} else {
						for (int k = 0; k < figureMoves.length; k++) {
							//Take vector
							int xMove = figureMoves[k][0];
							int yMove = figureMoves[k][1];
							// Add vector to the current position
							int x = j + xMove;
							int y = i + yMove;
							while (x >= 0 && x < 8 && y >= 0 && y < 8 && figureBoard[y][x] == null) {
								int[] tablePosition = {y, x};
								if (figureColor == "black") {
									possibleBeatFieldBlack.add(beatIteratorBlack,tablePosition);
									beatIteratorBlack++;
								} else {
									possibleBeatFieldWhite.add(beatIteratorWhite, tablePosition);
									beatIteratorWhite++;
								}
								// Move current position by vector again
								x += xMove;
								y += yMove;
							}
							// If the first figure that selected figure meets is opponent figure, it means we can run on that place
							// thus add that position to possible to move there
							if (x >= 0 && x < 8 && y >= 0 && y < 8 && figureBoard[y][x] != null && figureBoard[y][x].getColor() != figureColor) {
								int[] tablePosition = {y, x};
								if (figureColor == "black") {
									possibleBeatFieldBlack.add(beatIteratorBlack, tablePosition);
									beatIteratorBlack++;
								} else {
									possibleBeatFieldWhite.add(beatIteratorWhite, tablePosition);
									beatIteratorWhite++;
								}
							}
						}
					}
				}
			}
		}
	}
	private void markPossibleMovesOnBoard() {
		//Mark possible path in another color
		for (int i = 0; i < avaiableMoves.size(); i++) {
			int y = avaiableMoves.get(i)[0];
			int x = avaiableMoves.get(i)[1];
			rectBoard[y][x].setFill(Color.AQUAMARINE);
		}
	}
	private void unmarkPossibleMovesOnBoard() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if((j + i) % 2 == 0) {
					rectBoard[i][j].setFill(Color.ANTIQUEWHITE);
				} else {
					rectBoard[i][j].setFill(Color.BROWN);
				}
			}
		}
	}
	private void clearMoves() {
		//Clear every available moves
		avaiableMoves.removeAll(avaiableMoves);
	}
	private void moveFigure(int xPosition, int yPosition) {
		//Get selected figure
		Figure selectedFigure = board[selectedFigurePosition[0]][selectedFigurePosition[1]];
		
		//Move that figure on selected position in UI
		selectedFigure.setX(boxSize * xPosition + xToCenter);
		selectedFigure.setY(boxSize * yPosition + yToCenter);
		
		//Move that figure on selected position in board
		board[selectedFigurePosition[0]][selectedFigurePosition[1]] = null;
		if (board[yPosition][xPosition] != null) {
			this.getChildren().remove(board[yPosition][xPosition]);
		} else if (board[yPosition][xPosition] != null && board[yPosition][xPosition].getClass() == King.class) {
			return;
		}
		board[yPosition][xPosition] = selectedFigure;
		
		//Deselect figure
		selectedFigurePosition[0] = -1;
		selectedFigurePosition[0] = -1;
		
		//Check that after move check occurs
		isCheck = getIsCheck(board, move);
		if (isCheck && !canMove()) {
			System.out.println("Szach mat");
		}
		//Give next move for the opponent
		move = (move == 0) ? 1 : 0;
		clearMoves();
	}
	private boolean getIsCheck(Figure[][] board, int move) {
		checkPossibleBeatField(board);
		if (move == 0) {
			for (int i = 0; i < possibleBeatFieldWhite.size(); i++) {
				int y = possibleBeatFieldWhite.get(i)[0];
				int x = possibleBeatFieldWhite.get(i)[1];
				if (board[y][x] != null && board[y][x].getClass() == King.class && board[y][x].getColor() == "black") {
					// Check on black
					System.out.println("Szach na czarnych");
					return true;
				} 
			}
		} else {
			for (int i = 0; i < possibleBeatFieldBlack.size(); i++) {
				int y = possibleBeatFieldBlack.get(i)[0];
				int x = possibleBeatFieldBlack.get(i)[1];
				if (board[y][x] != null && board[y][x].getClass() == King.class && board[y][x].getColor() == "white") {
					// Check on white
					System.out.println("Szach na białych");
					return true;
				}
			}
		}
		return false;
	}
	private boolean canDefendCheck(int selectedFigurePosY, int selectedFigurePosX, int move) {
		// This method checks that selected figure can defend check
		
		Figure selectedFigure = board[selectedFigurePosY][selectedFigurePosX];
		List<int[]> movesThatCanDefendCheck = new ArrayList<int[]>();
		int j = 0;
		
		//Check that possible move can defend check
		for (int i = 0; i < avaiableMoves.size(); i++) {
			// Create copy of original board to make simulation about which move can defend check
			Figure[][] simulationBoard = new Figure[8][8];
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					simulationBoard[k][l] = board[k][l];
				}
			}
			
			int x = avaiableMoves.get(i)[1];
			int y = avaiableMoves.get(i)[0];
			
			// Move figure to one of the avaiableMoves
			simulationBoard[selectedFigurePosY][selectedFigurePosX] = null;
			simulationBoard[y][x] = selectedFigure;
			
			// Check whether check occurs
			if(!getIsCheck(simulationBoard, move)) {
				movesThatCanDefendCheck.add(j, new int[] {y, x});
				j++;
			}
		}
		avaiableMoves = movesThatCanDefendCheck;
		return movesThatCanDefendCheck.size() > 0;
	}
	private boolean canMove() {
		// Check whether figures of the same color can move somewhere to defend check
		String color = (move == 0) ? "black" : "white";
		// For every figure on board
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] != null && color == board[i][j].getColor()) {
					clearMoves();
					checkPossibleMoves(i, j);
					
					if (canDefendCheck(i, j, move == 0 ? 0 : 1)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
