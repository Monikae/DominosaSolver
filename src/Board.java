import java.util.LinkedList;

public class Board implements Cloneable {
	private int numColumns, numRows, highestNumber;
	private int[][] numbersOnBoard;

	PotentialDirections[][] potentialDirections;
	char[][] finalDirections;
	PositionList[][] potentialPositionsForTiles;

	public static Board createInitialBoard(int highestNumber, int numRows, int numColumns,
			int[][] numbersOnBoard)
	{
		Board board = new Board(highestNumber, numRows, numColumns, numbersOnBoard);
		board.initializeFirstBoard();
		return board;
	}
	
	private void initializeFirstBoard() {
	    finalDirections = new char[numRows][numColumns];

	    potentialDirections = new PotentialDirections[numRows][numColumns];
	    PotentialDirections.initialize(potentialDirections);

	    for (int i = 0; i < numColumns; i++)
	    {
	      potentialDirections[0][i].up = false;
	      potentialDirections[numRows - 1][i].down = false;
	    }
	    for (int i = 0; i < numRows; i++)
	    {
	      potentialDirections[i][0].left = false;
	      potentialDirections[i][numColumns - 1].right = false;
	    }

	    PotentialDirections.printPotentialDirections(potentialDirections);

	    potentialPositionsForTiles = new PositionList[highestNumber + 1][highestNumber + 1];

	    for (int i = 0; i <= highestNumber; i++)
	    {
	      for (int j = i; j <= highestNumber; j++)
	      {
	        potentialPositionsForTiles[i][j] = new PositionList();
	      }
	    }

	    // initialize horizontal tiles
	    for (int i = 0; i < numbersOnBoard.length; i++) // all lines/rows
	    {
	      for (int j = 0; j < numbersOnBoard[i].length - 1; j++) // all columns except the last one
	      {
	        int numberOnLeftTile = numbersOnBoard[i][j];
	        int numberOnRightTile = numbersOnBoard[i][j + 1];
	        if (numberOnLeftTile <= numberOnRightTile) potentialPositionsForTiles[numberOnLeftTile][numberOnRightTile].add(new Position(i, j,
	            OrientationEnum.HORIZONTAL));
	        else potentialPositionsForTiles[numberOnRightTile][numberOnLeftTile].add(new Position(i, j, OrientationEnum.HORIZONTAL));
	      }
	    }

	    // initialize vertical tiles
	    for (int i = 0; i < numbersOnBoard.length - 1; i++) // all lines/rows except the last one
	    {
	      for (int j = 0; j < numbersOnBoard[i].length; j++) // all columns 
	      {
	        int numberOnTopTile = numbersOnBoard[i][j];
	        int numberOnBottomTile = numbersOnBoard[i + 1][j];
	        if (numberOnTopTile <= numberOnBottomTile) potentialPositionsForTiles[numberOnTopTile][numberOnBottomTile].add(new Position(i, j,
	            OrientationEnum.VERTICAL));
	        else potentialPositionsForTiles[numberOnBottomTile][numberOnTopTile].add(new Position(i, j, OrientationEnum.VERTICAL));
	      }
	    }

	}
	
	private Board(int highestNumber, int numRows, int numColumns,
			int[][] numbersOnBoard) {
		super();
		assert numRows == highestNumber + 1;
		assert numColumns == numRows + 1;
		this.highestNumber = highestNumber;
		this.numRows = numRows;
		this.numColumns = numColumns;
		this.numbersOnBoard = numbersOnBoard;
	}

	public void removePotentialTile(Position position)
			throws UnsolvableException {
		if (position.orientation == OrientationEnum.HORIZONTAL) {
			removeHorizontalTile(position.row, position.column);
		} else if (position.orientation == OrientationEnum.VERTICAL) {
			removeVerticalTile(position.row, position.column);
		}
	}

	public void removeHorizontalTile(int r, int c) throws UnsolvableException {
		int numberOnLeftTile = numbersOnBoard[r][c];
		int numberOnRightTile = numbersOnBoard[r][c + 1];
		if (numberOnLeftTile <= numberOnRightTile) {
			potentialPositionsForTiles[numberOnLeftTile][numberOnRightTile]
					.remove(new Position(r, c, OrientationEnum.HORIZONTAL));
		} else {
			potentialPositionsForTiles[numberOnRightTile][numberOnLeftTile]
					.remove(new Position(r, c, OrientationEnum.HORIZONTAL));
		}
		potentialDirections[r][c].clearRight();
		potentialDirections[r][c + 1].clearLeft();
	}

	public void removeVerticalTile(int r, int c) throws UnsolvableException {
		int numberOnTopTile = numbersOnBoard[r][c];
		int numberOnBottomTile = numbersOnBoard[r + 1][c];
		if (numberOnTopTile <= numberOnBottomTile) {
			potentialPositionsForTiles[numberOnTopTile][numberOnBottomTile]
					.remove(new Position(r, c, OrientationEnum.VERTICAL));
		} else {
			potentialPositionsForTiles[numberOnBottomTile][numberOnTopTile]
					.remove(new Position(r, c, OrientationEnum.VERTICAL));
		}
		potentialDirections[r][c].clearDown();
		potentialDirections[r + 1][c].clearUp();
	}

	/**
	 * 
	 * @param firstNumber
	 * @param secondNumber
	 * @param row
	 * @param column
	 * @param orientation
	 * @return list of removed potential positions
	 * @throws UnsolvableException
	 */
	public void setUniqueCoordinate(int firstNumber, int secondNumber, int row,
			int column, OrientationEnum orientation) throws UnsolvableException {
		int smallerNumber = -1, biggerNumber = -1;
		if (firstNumber <= secondNumber) {
			smallerNumber = firstNumber;
			biggerNumber = secondNumber;
		} else {
			smallerNumber = secondNumber;
			biggerNumber = firstNumber;
		}

		Position thePosition = new Position(row, column, orientation);
		LinkedList<Position> removedPositions = potentialPositionsForTiles[smallerNumber][biggerNumber]
				.setUniqueCoordinate(thePosition);

		for (Position removedPosition : removedPositions) {
			this.removePotentialTile(removedPosition);
		}

		for (Square above : thePosition.getSquaresAbove()) {
			potentialDirections[above.row][above.column].clearDown();
		}
		for (Square below : thePosition.getSquaresBelow(numRows)) {
			potentialDirections[below.row][below.column].clearUp();
		}
		for (Square left : thePosition.getSquaresLeft()) {
			potentialDirections[left.row][left.column].clearRight();
		}
		for (Square right : thePosition.getSquaresRight(numColumns)) {
			potentialDirections[right.row][right.column].clearLeft();
		}
	}

	public SolveStatus isDone() {
		boolean potentialDirectionsUnique = true;
		boolean finalDirectionsSet = true;
		boolean potentialPositionsUnique = true;

		boolean zeroDirectionExists = false;
		boolean zeroPositionExists = false;

		out: for (PotentialDirections[] potentialDirectionsRow : potentialDirections) {
			for (PotentialDirections potentialDirectionsSquare : potentialDirectionsRow) {
				if (potentialDirectionsSquare.hasOnlyOneDirection() == false) {
					potentialDirectionsUnique = false;
				}
				if (potentialDirectionsSquare.hasNoDirections()) {
					zeroDirectionExists = true;
					break out;
				}
			}
		}

		for (char[] cs : finalDirections) {
			for (char c : cs) {
				if (c != '<' && c != '>' && c != '^' && c != 'V') {
					finalDirectionsSet = false;
				}
			}
		}

		out: for (int i = 0; i < potentialPositionsForTiles.length; i++) {
			for (int j = i; j < potentialPositionsForTiles[i].length; j++) {
				if (potentialPositionsForTiles[i][j].hasExactlyOne() == false) {
					potentialPositionsUnique = false;
				}
				if (potentialPositionsForTiles[i][j].isEmpty()) {
					zeroPositionExists = true;
					break out;
				}
			}
		}

		SolveStatus returnStatus = SolveStatus.INCOMPLETE;

		if (zeroDirectionExists || zeroPositionExists) {
			returnStatus = SolveStatus.CONFLICT;
		} else if (potentialDirectionsUnique || finalDirectionsSet
				|| potentialPositionsUnique) {
			returnStatus = SolveStatus.SOLVED;

			// they should all become true at the same time
			if (!potentialDirectionsUnique || !finalDirectionsSet
					|| !potentialPositionsUnique) {
				System.out
						.println("Something is wrong, either all three criteria should say it's solved or none!");
			}
		}

		return returnStatus;
	}

	@Override
	public Board clone() {
		Board b = new Board(highestNumber, numRows, numColumns, numbersOnBoard);
		// numbersOnBoard isn't changed anyway, doesn't need to be cloned

		b.potentialDirections = new PotentialDirections[numRows][numColumns];
		b.finalDirections = new char[numRows][numColumns];
		b.potentialPositionsForTiles = new PositionList[highestNumber + 1][highestNumber + 1];

		copy(potentialDirections, b.potentialDirections, finalDirections,
				b.finalDirections, potentialPositionsForTiles,
				b.potentialPositionsForTiles);

		return b;
	}

	public void applyState(Board otherBoard) {
		copy(otherBoard.potentialDirections, this.potentialDirections,
				otherBoard.finalDirections, this.finalDirections,
				otherBoard.potentialPositionsForTiles,
				this.potentialPositionsForTiles);

	}

	private static void copy(PotentialDirections[][] potentialDirections,
			PotentialDirections[][] newPotentialDirections,
			char[][] finalDirections, char[][] newFinalDirections,
			PositionList[][] potentialPositionsForTiles,
			PositionList[][] newPotentialPositionsForTiles) {

		assert potentialDirections != null;
		assert newPotentialDirections != null;
		assert potentialDirections != newPotentialDirections;
		assert potentialDirections.length == newPotentialDirections.length;
		for (int i = 0; i < potentialDirections.length; i++) {
			assert potentialDirections[i].length == newPotentialDirections[i].length;
			for (int j = 0; j < potentialDirections[i].length; j++) {
				newPotentialDirections[i][j] = (PotentialDirections) potentialDirections[i][j]
						.clone();
			}
		}

		assert finalDirections != null;
		assert newFinalDirections != null;
		assert finalDirections != newFinalDirections;
		assert finalDirections.length == newFinalDirections.length;
		for (int i = 0; i < finalDirections.length; i++) {
			assert finalDirections[i].length == newFinalDirections[i].length;
			for (int j = 0; j < finalDirections[i].length; j++) {
				newFinalDirections[i][j] = finalDirections[i][j];
			}
		}

		assert potentialPositionsForTiles != null;
		assert newPotentialPositionsForTiles != null;
		assert potentialPositionsForTiles != newPotentialPositionsForTiles;
		assert potentialPositionsForTiles.length == newPotentialPositionsForTiles.length;
		for (int i = 0; i < potentialPositionsForTiles.length; i++) {
			assert potentialPositionsForTiles[i].length == newPotentialPositionsForTiles[i].length;
			for (int j = i; j < potentialPositionsForTiles[i].length; j++) {
				newPotentialPositionsForTiles[i][j] = (PositionList) potentialPositionsForTiles[i][j]
						.clone();
			}
		}

	}
}
