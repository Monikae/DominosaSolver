package dominosa_solver;
import static def.dom.Globals.console;

public class Board 
{
  public static int numColumns, numRows, highestNumber;

  public static int[][] numbersOnBoard;

  PotentialDirections[][] potentialDirections;

  char[][] finalDirections;

  PositionList[][] potentialPositionsForTiles;
  
  static boolean printSteps; // print (some of) the steps so a human can follow
  
  public Board(Board b) {

	    this.potentialDirections = new PotentialDirections[numRows][numColumns];
	    this.finalDirections = new char[numRows][numColumns];
	    this.potentialPositionsForTiles = new PositionList[highestNumber + 1][highestNumber + 1];

	    copy(b.potentialDirections, this.potentialDirections, b.finalDirections, this.finalDirections, b.potentialPositionsForTiles,  this.potentialPositionsForTiles);
	  
  }
  /**
   * 
   * @return solved / incomplete / conflict
   */
  private SolveStatus seekUnique()
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
              if (printSteps)
            	  console.log("Only one potential position: ");
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
              if (printSteps)
              console.log("Only one potential direction: ");              
              potDir.done = true;
              changed = true;
              int top = -1, left = -1;
              Orientation dir = null;
              if (potDir.right)
              {
                top = r;
                left = c;
                dir = Orientation.HORIZONTAL;
              }
              if (potDir.left)
              {
                top = r;
                left = c - 1;
                dir = Orientation.HORIZONTAL;
              }
              if (potDir.up)
              {
                top = r - 1;
                left = c;
                dir = Orientation.VERTICAL;
              }
              if (potDir.down)
              {
                top = r;
                left = c;
                dir = Orientation.VERTICAL;
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
    return solveStatus;
  }

  /**
   * 
   * @return solved / incomplete / conflict
   */
  public SolveStatus seek()
  {
    SolveStatus seekUniqueSolveStatus = seekUnique();
      
    if (seekUniqueSolveStatus == SolveStatus.SOLVED)
    {
      return SolveStatus.SOLVED;
    }

    if (seekUniqueSolveStatus == SolveStatus.CONFLICT)
    {
      return SolveStatus.CONFLICT;
    }

    // recursion
    if (seekUniqueSolveStatus == SolveStatus.INCOMPLETE)
    {
      Board newBoard = null;
      SolveStatus clonedBoardSolveStatus = null;
      SolveStatus seekUniqueSolveStatus2 = null;
      while (true) // will be left with return
      {
        Position guess = guess(); // doesn't change the board

        if (guess == null)
        {
          return SolveStatus.CONFLICT;
        }

        newBoard = new Board(this);
        if (printSteps)
        	console.log("--> Making a guess: ");
        newBoard.applyGuess(guess); // like setUniquePosition() without exception
        clonedBoardSolveStatus = newBoard.seek(); // recursion

        assert (clonedBoardSolveStatus != SolveStatus.INCOMPLETE);

        if (clonedBoardSolveStatus == SolveStatus.SOLVED)
        {
          // accept the guess
          if(printSteps)
            System.out.println("This guess worked.");
          this.applyState(newBoard);
          return SolveStatus.SOLVED;
        }
        if (clonedBoardSolveStatus == SolveStatus.CONFLICT)
        {
          if(printSteps)
            System.out.println("<-- This guess didn't work.");
          
          // mark the guessed location as impossible - on the original!
          this.markGuessAsImpossible(guess);

          // maybe this made the position unique or the potential directions for a square
          seekUniqueSolveStatus2 = this.seekUnique();
          
          switch (seekUniqueSolveStatus2)
          {
            case SOLVED: // the only alternative to the (failed) guess is the solution
              return SolveStatus.SOLVED;
            case CONFLICT: // the alternative to the (failed) guess doesn't work 
              if(printSteps)
                System.out.println("<-- This guess didn't work. The alternative didn't work either.");              
              // => neither the guess nor the alternative to the guess work
              // => this branch/recursion doesn't work at all 
              // (no sense in trying to apply a different guess to this board)
              return SolveStatus.CONFLICT;
            case INCOMPLETE: // this (failed) guess helped us reduce the number of choices
              if(printSteps)
                System.out.println("<-- This guess didn't work, but helped us narrow down the possibilities.");                
              // apply the next guess => go to beginning of loop (guess and clone again)
              // == do nothing ==
              break;
          }

        } // end if CONFLICT (for guess)
      } // end while(true)
    } // end if INCOMPLETE (after seekUnique())

    // not actually reachable
    assert false;
    return seekUniqueSolveStatus;
  }

  /**
   * doesn't change the board
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

  /**
   * like removePotentialTile() without the exception
   */
  private void markGuessAsImpossible(Position guess)
  {
    try
    {
      removePotentialTile(guess);
    }
    catch (UnsolvableException ex)
    {
      assert false;
    }
  }

  private void removePotentialTile(Position position) throws UnsolvableException
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
   * like setUniquePosition() but no exception possible
   * @param guess
   */
  private void applyGuess(Position guess)
  {
    try
    {
      setUniquePosition(guess);
    }
    catch (UnsolvableException ex)
    {
      assert false;
    }
  }

  /**
   * 
   * @param position
   * @return list of removed potential positions
   * @throws UnsolvableException
   */
  private void setUniquePosition(Position uniquePosition) throws UnsolvableException
  {
    int smallerNumber = uniquePosition.getSmallerNumber();
    int biggerNumber = uniquePosition.getBiggerNumber();
    setUniquePosition(smallerNumber, biggerNumber, uniquePosition);
  }

  private void setUniquePosition(int smallerNumber, int biggerNumber, Position uniquePosition) throws UnsolvableException
  {
    if (printSteps) 
      System.out.println("Placing tile ["+smallerNumber+" "+biggerNumber+"] at ("+uniquePosition.row+"/"+uniquePosition.column+") "+uniquePosition.orientation.getAdverb()+" (row/column) (counting from 0).");    
    
    assert smallerNumber <= biggerNumber;

    def.js.Array<Position> removedPositions
      = potentialPositionsForTiles[smallerNumber][biggerNumber].setUniquePosition(uniquePosition);
    potentialPositionsForTiles[smallerNumber][biggerNumber].done = true;

    for (Position removedPosition : removedPositions)
    {
      removePotentialTile(removedPosition);
    }

    int row = uniquePosition.row, column = uniquePosition.column;
    if (uniquePosition.orientation == Orientation.HORIZONTAL)
    {
      finalDirections[row][column] = DirectionEnum.RIGHT.value();
      finalDirections[row][column + 1] = DirectionEnum.LEFT.value();
      potentialDirections[row][column].clearAllExceptRight();
      potentialDirections[row][column + 1].clearAllExceptLeft();
    }
    else if (uniquePosition.orientation == Orientation.VERTICAL)
    {
      finalDirections[row][column] = DirectionEnum.DOWN.value();
      finalDirections[row + 1][column] = DirectionEnum.UP.value();
      potentialDirections[row][column].clearAllExceptDown();
      potentialDirections[row + 1][column].clearAllExceptUp();
    }

    // directions in neighbors squares + overlapping positions
    def.js.Array<Position> overlappingPositions = uniquePosition.getOverlappingPositions();
    for (Position overlapPos : overlappingPositions)
    {
      removePotentialTile(overlapPos);
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
        if (c == DirectionEnum.NOT_SET.value())
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

  private void applyState(Board otherBoard)
  {
    this.potentialDirections = otherBoard.potentialDirections;
    this.finalDirections = otherBoard.finalDirections;
    this.potentialPositionsForTiles = otherBoard.potentialPositionsForTiles;
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
        newPotentialDirections[i][j] = new PotentialDirections(potentialDirections[i][j]);
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
        newPotentialPositionsForTiles[i][j] = new PositionList(potentialPositionsForTiles[i][j]) ;
      }
    }

  }
}
