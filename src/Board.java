
public class Board implements Cloneable
{
  public static int numColumns, numRows, highestNumber;

  public static int[][] numbersOnBoard;

  PotentialDirections[][] potentialDirections;

  char[][] finalDirections;

  PositionList[][] potentialPositionsForTiles;

  /**
   * 
   * @return solved / incomplete / conflict
   */
  public SolveStatus seek()
  {
    SolveStatus solveStatus = SolveStatus.INCOMPLETE;

    try
    {
      boolean changed = true;
      while (changed == true)
      {
        changed = false;

        // tiles that have only one potential coordinate
        for (int i = 0; i <= highestNumber; i++)
        {
          for (int j = i; j <= highestNumber; j++)
          {
            if (potentialPositionsForTiles[i][j].hasExactlyOne() && potentialPositionsForTiles[i][j].done == false)
            {
              potentialPositionsForTiles[i][j].done = true;
              changed = true;
              Position position = potentialPositionsForTiles[i][j].getFirst();
              setUniquePosition(i, j, position);
            }
          }
        }

        // squares that have only one potential direction
        for (int r = 0; r < numRows; r++)
        {
          for (int c = 0; c < numColumns; c++)
          {
            PotentialDirections potDir = potentialDirections[r][c];
            if (potDir.done == false && potDir.hasOnlyOneDirection() == true)
            {
              potDir.done = true;
              changed = true;
              finalDirections[r][c] = potDir.oneDirection();
              int top = -1, left = -1;
              Orientation dir = null;
              if (potDir.right)
              {
                top = r;
                left = c;
                dir = Orientation.HORIZONTAL;
                potentialDirections[r][c + 1].clearAllExceptLeft();
              }
              if (potDir.left)
              {
                top = r;
                left = c - 1;
                dir = Orientation.HORIZONTAL;
                potentialDirections[r][c - 1].clearAllExceptRight();
              }
              if (potDir.up)
              {
                top = r - 1;
                left = c;
                dir = Orientation.VERTICAL;
                potentialDirections[r - 1][c].clearAllExceptDown();
              }
              if (potDir.down)
              {
                top = r;
                left = c;
                dir = Orientation.VERTICAL;
                potentialDirections[r + 1][c].clearAllExceptUp();
              }
              setUniquePosition(new Position(top, left, dir));

            }
          }
        }
      } // while (changed == true)

      solveStatus = isDone();
    }
    catch (UnsolvableException ex)
    {
      solveStatus = SolveStatus.CONFLICT;
    }

    // ///////////////////////////////////////

    if (solveStatus == SolveStatus.SOLVED)
    {
      return SolveStatus.SOLVED;
    }

    // recursion
    if (solveStatus == SolveStatus.INCOMPLETE)
    {
      Board newBoard = this.clone();

      Position guess = newBoard.guess();

      if (guess == null)
      {
        return SolveStatus.CONFLICT;
      }

      try
      {
        newBoard.setUniquePosition(guess);
      }
      catch (UnsolvableException ex)
      {
        System.out.println("Doesn't happen (newBoard.setUniquePosition)");
      }

      solveStatus = newBoard.seek();

      if (solveStatus == SolveStatus.INCOMPLETE)
      {
        System.out.println("This shouldn't happen, after the recursion the status should either be solved or conflict, not incomplete.");
      }
      if (solveStatus == SolveStatus.SOLVED)
      {
        // accept the guess
        applyState(newBoard);
      }
      if (solveStatus == SolveStatus.CONFLICT)
      {
        // mark the guessed location as impossible - on the original!
        try
        {
          removePotentialTile(guess);
        }
        catch (UnsolvableException ex)
        {
          System.out.println("Can't happen - marking guess impossible");
        }
      }
      return solveStatus; // CONFLICT or SOLVED
    }

    if (solveStatus == SolveStatus.CONFLICT)
    {
      return SolveStatus.CONFLICT;
    }

    // not actually reachable
    return solveStatus;
  }

  /**
   * 
   * @return null if it was not possible to make a guess
   */
  private Position guess()
  {
    PositionList positionListLength2 = null;
    PositionList lastPositionListLength3orMore = null;
    out: for (int row = 0; row < potentialPositionsForTiles.length; row++)
    {
      for (int column = 0; column < potentialPositionsForTiles[row].length; column++)
      {
        PositionList positionList = potentialPositionsForTiles[row][column];
        int size = positionList.size();
        if (size == 0 || size == 1)
        {
          continue;
        }
        if (size == 2)
        {
          positionListLength2 = positionList;
          break out;
        }
        if (size >= 3)
        {
          lastPositionListLength3orMore = positionList;
        }
      }
    }

    Position position = null;
    if (positionListLength2 != null)
    {
      position = positionListLength2.getFirst();
    }
    else if (lastPositionListLength3orMore != null)
    {
      position = lastPositionListLength3orMore.getFirst();
    }
    else
    {
      return null;
    }
    return position;
  }

  public static Board createInitialBoard(int highestNumber, int numRows, int numColumns, int[][] numbersOnBoard)
  {
    Board.highestNumber = highestNumber;
    Board.numRows = numRows;
    Board.numColumns = numColumns;
    Board.numbersOnBoard = numbersOnBoard;
    Board board = new Board();
    board.initializeFirstBoard();
    return board;
  }

  private void initializeFirstBoard()
  {
    finalDirections = new char[numRows][numColumns];
    for (int i = 0; i < finalDirections.length; i++)
    {
      for (int j = 0; j < finalDirections[i].length; j++)
      {
        finalDirections[i][j] = '?';
      }
    }

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
      for (int j = 0; j < numbersOnBoard[i].length - 1; j++) // all
      // columns
      // except
      // the last
      // one
      {
        int numberOnLeftTile = numbersOnBoard[i][j];
        int numberOnRightTile = numbersOnBoard[i][j + 1];
        if (numberOnLeftTile <= numberOnRightTile)
          potentialPositionsForTiles[numberOnLeftTile][numberOnRightTile].add(new Position(i, j, Orientation.HORIZONTAL));
        else potentialPositionsForTiles[numberOnRightTile][numberOnLeftTile].add(new Position(i, j, Orientation.HORIZONTAL));
      }
    }

    // initialize vertical tiles
    for (int i = 0; i < numbersOnBoard.length - 1; i++) // all lines/rows
    // except the last
    // one
    {
      for (int j = 0; j < numbersOnBoard[i].length; j++) // all columns
      {
        int numberOnTopTile = numbersOnBoard[i][j];
        int numberOnBottomTile = numbersOnBoard[i + 1][j];
        if (numberOnTopTile <= numberOnBottomTile)
          potentialPositionsForTiles[numberOnTopTile][numberOnBottomTile].add(new Position(i, j, Orientation.VERTICAL));
        else potentialPositionsForTiles[numberOnBottomTile][numberOnTopTile].add(new Position(i, j, Orientation.VERTICAL));
      }
    }

  }

  private Board()
  {
    super();
  }

  public void removePotentialTile(Position position) throws UnsolvableException
  {
    potentialPositionsForTiles[position.getSmallerNumber()][position.getBiggerNumber()].remove(position);
    
    PotentialDirections potDir1 = potentialDirections[position.row][position.column];
    PotentialDirections potDir2 = potentialDirections[position.secondRow()][position.secondColumn()];
    if (position.orientation == Orientation.HORIZONTAL)
    {
      potDir1.clearRight();
      potDir2.clearLeft();
    }
    else if (position.orientation == Orientation.VERTICAL)
    {
      potDir1.clearDown();
      potDir2.clearUp();
    }    
  }

  /**
   * 
   * @param position
   * @return list of removed potential positions
   * @throws UnsolvableException
   */
  public void setUniquePosition(Position uniquePosition) throws UnsolvableException
  {
    int smallerNumber = uniquePosition.getSmallerNumber();
    int biggerNumber = uniquePosition.getBiggerNumber();
    setUniquePosition(smallerNumber, biggerNumber, uniquePosition);
  }
  
  public void setUniquePosition(int smallerNumber, int biggerNumber, Position uniquePosition) throws UnsolvableException
  {  
    assert smallerNumber < biggerNumber;
    
    Iterable<Position> removedPositions = potentialPositionsForTiles[smallerNumber][biggerNumber].setUniquePosition(uniquePosition);

    for (Position removedPosition : removedPositions)
    {
      removePotentialTile(removedPosition);
    }

    int row = uniquePosition.row, column = uniquePosition.column;
    if (uniquePosition.orientation == Orientation.HORIZONTAL)
    {
      finalDirections[row][column] = '>';
      finalDirections[row][column + 1] = '<';
      potentialDirections[row][column].clearAllExceptRight();
      potentialDirections[row][column + 1].clearAllExceptLeft();
    }
    else if (uniquePosition.orientation == Orientation.VERTICAL)
    {
      finalDirections[row][column] = 'V';
      finalDirections[row + 1][column] = '^';
      potentialDirections[row][column].clearAllExceptDown();
      potentialDirections[row + 1][column].clearAllExceptUp();
    }

    // neighbors
    for (Square above : uniquePosition.getSquaresAbove())
    {
      potentialDirections[above.row][above.column].clearDown();
    }
    for (Square below : uniquePosition.getSquaresBelow())
    {
      potentialDirections[below.row][below.column].clearUp();
    }
    for (Square left : uniquePosition.getSquaresLeft())
    {
      potentialDirections[left.row][left.column].clearRight();
    }
    for (Square right : uniquePosition.getSquaresRight())
    {
      potentialDirections[right.row][right.column].clearLeft();
    }

    // overlapping positions
    Iterable<Position> overlappingPositions = uniquePosition.getOverlappingPositions();
    for (Position overlapPos : overlappingPositions)
    {
      potentialPositionsForTiles[overlapPos.getSmallerNumber()][overlapPos.getBiggerNumber()].remove(overlapPos);
    }
  }

  public SolveStatus isDone()
  {
    boolean potentialDirectionsUnique = true;
    boolean finalDirectionsSet = true;
    boolean potentialPositionsUnique = true;

    boolean zeroDirectionExists = false;
    boolean zeroPositionExists = false;

    out: for (PotentialDirections[] potentialDirectionsRow : potentialDirections)
    {
      for (PotentialDirections potentialDirectionsSquare : potentialDirectionsRow)
      {
        if (potentialDirectionsSquare.hasOnlyOneDirection() == false)
        {
          potentialDirectionsUnique = false;
        }
        if (potentialDirectionsSquare.hasNoDirections())
        {
          zeroDirectionExists = true;
          break out;
        }
      }
    }

    for (char[] cs : finalDirections)
    {
      for (char c : cs)
      {
        if (c != '<' && c != '>' && c != '^' && c != 'V')
        {
          finalDirectionsSet = false;
        }
      }
    }

    out: for (int i = 0; i < potentialPositionsForTiles.length; i++)
    {
      for (int j = i; j < potentialPositionsForTiles[i].length; j++)
      {
        if (potentialPositionsForTiles[i][j].hasExactlyOne() == false)
        {
          potentialPositionsUnique = false;
        }
        if (potentialPositionsForTiles[i][j].isEmpty())
        {
          zeroPositionExists = true;
          break out;
        }
      }
    }

    SolveStatus returnStatus = SolveStatus.INCOMPLETE;

    if (zeroDirectionExists || zeroPositionExists)
    {
      returnStatus = SolveStatus.CONFLICT;
    }
    else if (potentialDirectionsUnique || finalDirectionsSet || potentialPositionsUnique)
    {
      returnStatus = SolveStatus.SOLVED;

      // they should all become true at the same time
      if (!potentialDirectionsUnique || !finalDirectionsSet || !potentialPositionsUnique)
      {
        System.out.println("Something is wrong, either all three criteria should say it's solved or none!");
      }
    }

    return returnStatus;
  }

  @Override public Board clone()
  {
    Board b = new Board();

    b.potentialDirections = new PotentialDirections[numRows][numColumns];
    b.finalDirections = new char[numRows][numColumns];
    b.potentialPositionsForTiles = new PositionList[highestNumber + 1][highestNumber + 1];

    copy(potentialDirections, b.potentialDirections, finalDirections, b.finalDirections, potentialPositionsForTiles, b.potentialPositionsForTiles);

    return b;
  }

  public void applyState(Board otherBoard)
  {
    copy(otherBoard.potentialDirections, this.potentialDirections, otherBoard.finalDirections, this.finalDirections, otherBoard.potentialPositionsForTiles,
        this.potentialPositionsForTiles);

  }

  private static void copy(PotentialDirections[][] potentialDirections, PotentialDirections[][] newPotentialDirections, char[][] finalDirections,
      char[][] newFinalDirections, PositionList[][] potentialPositionsForTiles, PositionList[][] newPotentialPositionsForTiles)
  {

    assert potentialDirections != null;
    assert newPotentialDirections != null;
    assert potentialDirections != newPotentialDirections;
    assert potentialDirections.length == newPotentialDirections.length;
    for (int i = 0; i < potentialDirections.length; i++)
    {
      assert potentialDirections[i].length == newPotentialDirections[i].length;
      for (int j = 0; j < potentialDirections[i].length; j++)
      {
        newPotentialDirections[i][j] = (PotentialDirections) potentialDirections[i][j].clone();
      }
    }

    assert finalDirections != null;
    assert newFinalDirections != null;
    assert finalDirections != newFinalDirections;
    assert finalDirections.length == newFinalDirections.length;
    for (int i = 0; i < finalDirections.length; i++)
    {
      assert finalDirections[i].length == newFinalDirections[i].length;
      for (int j = 0; j < finalDirections[i].length; j++)
      {
        newFinalDirections[i][j] = finalDirections[i][j];
      }
    }

    assert potentialPositionsForTiles != null;
    assert newPotentialPositionsForTiles != null;
    assert potentialPositionsForTiles != newPotentialPositionsForTiles;
    assert potentialPositionsForTiles.length == newPotentialPositionsForTiles.length;
    for (int i = 0; i < potentialPositionsForTiles.length; i++)
    {
      assert potentialPositionsForTiles[i].length == newPotentialPositionsForTiles[i].length;
      for (int j = i; j < potentialPositionsForTiles[i].length; j++)
      {
        newPotentialPositionsForTiles[i][j] = (PositionList) potentialPositionsForTiles[i][j].clone();
      }
    }

  }
}
