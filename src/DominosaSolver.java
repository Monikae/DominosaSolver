import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Scanner;

public class DominosaSolver
{
  static int numColumns, numRows, highestNumber;

  static int[][] numbersOnBoard;

  /**
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException
  {
    readInput();
    processInput();
  }

  static void test(int numColumnsTest, int numRowsTest, int highestNumberTest, int[][] linesTest)
  {
    DominosaSolver.numColumns = numColumnsTest;
    DominosaSolver.numRows = numRowsTest;
    DominosaSolver.highestNumber = highestNumberTest;
    DominosaSolver.numbersOnBoard = linesTest;

    processInput();
  }

  private static void readInput() throws IOException
  {
    @SuppressWarnings("resource") Scanner input = new Scanner(System.in);
    System.out.print("How many columns? > ");
    numColumns = input.nextInt();
    if (numColumns < 1) return;
    System.out.print("How many rows? > ");
    numRows = input.nextInt();
    if (numRows < 1) return;
    System.out.print("Highest number (starting from 0)? > ");
    highestNumber = input.nextInt();
    if (highestNumber < 0) return;
    if (highestNumber > 99)
    {
      System.out.println("Keep the highest number under 100");
      return;
    }

    int numberOfTilesInGivenRectangle = (numColumns * numRows) / 2;
    int numberOfTilesForGivenHighestNumber = (highestNumber + 1) /* tiles with two identical numbers */
        + (highestNumber + 1) * highestNumber / 2 /* tiles with two different numbers */;

    int numberOfTiles = numberOfTilesInGivenRectangle;

    if (numberOfTilesInGivenRectangle == numberOfTilesForGivenHighestNumber)
    {
      System.out.println(numColumns + " columns x " + numRows + " rows with highest number " + highestNumber);
      System.out.println("There are " + numberOfTiles + " different tiles.");
    }
    else
    {
      System.out.println("This rectangle with " + numColumns + " columns " + " x " + numRows + "can fit " + numberOfTilesInGivenRectangle + "tiles,");
      System.out.println("but there are " + numberOfTilesForGivenHighestNumber + " different tiles with the numbers from 0 to " + highestNumber);
    }

    InputModeEnum inputMode = InputModeEnum.DEFAULT;
    if (highestNumber <= 9)
    {
      inputMode = InputModeEnum.DEFAULT; // default: no spaces, no letters to substitute for numbers, no leading zeroes
    }
    else
    {
      System.out.print(
          "If you want to put spaces between numbers enter s, if you want to enter numbers 10, 11, 12 etc. as a, b, c enter l, if you want to enter leading zeroes enter 0: s/l/0 > ");
      String spacesOrLetters = input.next();
      if ("s".equals(spacesOrLetters))
      {
        inputMode = InputModeEnum.SPACES;
      }
      else if ("l".equals(spacesOrLetters))
      {
        inputMode = InputModeEnum.LETTERS;
      }
      else if ("0".equals(spacesOrLetters))
      {
        inputMode = InputModeEnum.ZEROES;
      }
      else
      {
        System.out.println("Enter s or l or 0");
        return;
      }
    }

    String[] inputLines = new String[numRows];
    for (int i = 0; i < inputLines.length; i++)
    {
      System.out.print("Enter line " + (i + 1) + " > ");
      if (inputMode == InputModeEnum.SPACES)
      {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        inputLines[i] = br.readLine();
      }
      else
      {
        inputLines[i] = input.next();
      }
    }

    numbersOnBoard = new int[numRows][numColumns];
    for (int i = 0; i < numbersOnBoard.length; i++)
    {
      numbersOnBoard[i] = parse(inputLines[i], inputMode, numColumns);
    }

    for (int i = 0; i < inputLines.length; i++)
    {
      System.out.println(print(numbersOnBoard[i], false));
    }

  }

  static void processInput()
  {

    Board board = Board.createInitialBoard(highestNumber, numRows, numColumns, numbersOnBoard);

    SolveStatus solveStatus = board.seek();

    switch (solveStatus)
    {
      case INCOMPLETE:
        System.out.println("Final status is incomplete - should not happen.");
        break;
      case CONFLICT:
        System.out.println("Final status is unsolvable.");
        break;
      case SOLVED:
        System.out.println("Final status is solved.");
        break;
      default:
        System.out.println("Final status is undefined - should not happen.");
    }

    System.out.println("End - potential directions:");
    PotentialDirections.printPotentialDirections(board.potentialDirections);
    System.out.println("End - final directions:");
    PotentialDirections.printFinalDirections(board.finalDirections);
  }

  /**
   * 
   * @return solved / incomplete / conflict
   */
  private static SolveStatus seek(PotentialDirections[][] potentialDirections, char[][] finalDirections, PositionList[][] potentialPositionsForTiles)
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
              Position coord = potentialPositionsForTiles[i][j].getFirst();
              int row = coord.row;
              int col = coord.column;
              if (coord.orientation == Orientation.HORIZONTAL)
              {
                finalDirections[row][col] = '>';
                finalDirections[row][col + 1] = '<';
                potentialDirections[row][col].clearAllExceptRight();
                potentialDirections[row][col + 1].clearAllExceptLeft();
                // squares above (if they exist)
                if (valid(row - 1, col))
                {
                  potentialDirections[row - 1][col].clearDown();
                  removeVerticalTile(numbersOnBoard, potentialPositionsForTiles, row - 1, col);
                }
                if (valid(row - 1, col + 1))
                {
                  potentialDirections[row - 1][col + 1].clearDown();
                  removeVerticalTile(numbersOnBoard, potentialPositionsForTiles, row - 1, col + 1);
                }
                // squares below (if they exist)
                if (valid(row + 1, col))
                {
                  potentialDirections[row + 1][col].clearUp();
                  removeVerticalTile(numbersOnBoard, potentialPositionsForTiles, row, col);
                }
                if (valid(row + 1, col + 1))
                {
                  potentialDirections[row + 1][col + 1].clearUp();
                  removeVerticalTile(numbersOnBoard, potentialPositionsForTiles, row, col + 1);
                }
                // square to the left (if it exists)
                if (valid(row, col - 1))
                {
                  potentialDirections[row][col - 1].clearRight();
                  removeHorizontalTile(numbersOnBoard, potentialPositionsForTiles, row, col - 1);
                }
                // square to the right (if it exists)
                if (valid(row, col + 2))
                {
                  potentialDirections[row][col + 2].clearLeft();
                  removeHorizontalTile(numbersOnBoard, potentialPositionsForTiles, row, col + 1);
                }
              }
              else if (coord.orientation == Orientation.VERTICAL)
              {
                finalDirections[row][col] = 'V';
                finalDirections[row + 1][col] = '^';
                potentialDirections[row][col].clearAllExceptDown();
                potentialDirections[row + 1][col].clearAllExceptUp();
                // square above (if it exists)
                if (valid(row - 1, col))
                {
                  potentialDirections[row - 1][col].clearDown();
                  removeVerticalTile(numbersOnBoard, potentialPositionsForTiles, row - 1, col);
                }
                // square below (if it exists)
                if (valid(row + 2, col))
                {
                  potentialDirections[row + 2][col].clearUp();
                  removeVerticalTile(numbersOnBoard, potentialPositionsForTiles, row + 1, col);
                }
                // squares to the left (if they exists)
                if (valid(row, col - 1))
                {
                  potentialDirections[row][col - 1].clearRight();
                  removeHorizontalTile(numbersOnBoard, potentialPositionsForTiles, row, col - 1);
                }
                if (valid(row + 1, col - 1))
                {
                  potentialDirections[row + 1][col - 1].clearRight();
                  removeHorizontalTile(numbersOnBoard, potentialPositionsForTiles, row + 1, col - 1);
                }
                // squares to the right (if they exists)
                if (valid(row, col + 1))
                {
                  potentialDirections[row][col + 1].clearLeft();
                  removeHorizontalTile(numbersOnBoard, potentialPositionsForTiles, row, col);
                }
                if (valid(row + 1, col + 1))
                {
                  potentialDirections[row + 1][col + 1].clearLeft();
                  removeHorizontalTile(numbersOnBoard, potentialPositionsForTiles, row + 1, col);
                }
              }
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
              int thisNumber = numbersOnBoard[r][c];
              int otherNumber = -1;
              int top = -1, left = -1;
              Orientation dir = null;
              if (potDir.right)
              {
                otherNumber = numbersOnBoard[r][c + 1];
                top = r;
                left = c;
                dir = Orientation.HORIZONTAL;
                potentialDirections[r][c + 1].clearAllExceptLeft();
              }
              if (potDir.left)
              {
                otherNumber = numbersOnBoard[r][c - 1];
                top = r;
                left = c - 1;
                dir = Orientation.HORIZONTAL;
                potentialDirections[r][c - 1].clearAllExceptRight();
              }
              if (potDir.up)
              {
                otherNumber = numbersOnBoard[r - 1][c];
                top = r - 1;
                left = c;
                dir = Orientation.VERTICAL;
                potentialDirections[r - 1][c].clearAllExceptDown();
              }
              if (potDir.down)
              {
                otherNumber = numbersOnBoard[r + 1][c];
                top = r;
                left = c;
                dir = Orientation.VERTICAL;
                potentialDirections[r + 1][c].clearAllExceptUp();
              }
              LinkedList<Position> removedPositions = setUniqueCoordinate(potentialPositionsForTiles, thisNumber, otherNumber, top, left, dir);
              for (Position position : removedPositions)
              {
                Square[] squares = position.getSquares();
                Square first = squares[0];
                Square second = squares[1];
                if (position.orientation == Orientation.HORIZONTAL)
                {
                  potentialDirections[first.row][first.column].clearRight();
                  potentialDirections[second.row][second.column].clearLeft();
                }
                else if (position.orientation == Orientation.VERTICAL)
                {
                  potentialDirections[first.row][first.column].clearDown();
                  potentialDirections[second.row][second.column].clearUp();
                }
              }
              // in the next loop the unique coordinate will be handled, the directions on the neighbors set and so on
            }
          }
        }

      }

      solveStatus = isDone(potentialDirections, finalDirections, potentialPositionsForTiles);
    }
    catch (UnsolvableException ex)
    {
      solveStatus = SolveStatus.CONFLICT;
    }

    /////////////////////////////////////////

    if (solveStatus == SolveStatus.SOLVED)
    {
      return SolveStatus.SOLVED;
    }

    // recursion
    if (solveStatus == SolveStatus.INCOMPLETE)
    {
      PotentialDirections[][] newPotentialDirections = new PotentialDirections[numRows][numColumns];
      char[][] newFinalDirections = new char[numRows][numColumns];
      PositionList[][] newPotentialPositionsForTiles = new PositionList[highestNumber + 1][highestNumber + 1];
      copy(potentialDirections, newPotentialDirections, finalDirections, newFinalDirections, potentialPositionsForTiles, newPotentialPositionsForTiles);

      Guess guess = guess(newPotentialDirections, newFinalDirections, newPotentialPositionsForTiles);

      if (guess == null)
      {
        return SolveStatus.CONFLICT;
      }

      solveStatus = seek(newPotentialDirections, newFinalDirections, newPotentialPositionsForTiles);

      if (solveStatus == SolveStatus.INCOMPLETE)
      {
        System.out.println("This shouldn't happen, after the recursion the status should either be solved or conflict, not incomplete.");
      }
      if (solveStatus == SolveStatus.SOLVED)
      {
        // accept the guess
        copy(newPotentialDirections, potentialDirections, newFinalDirections, finalDirections, newPotentialPositionsForTiles, potentialPositionsForTiles);
      }
      if (solveStatus == SolveStatus.CONFLICT)
      {
        // mark the guessed location as impossible - on the original!
        //  guess.markGuessImpossible(potentialDirections);
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
   * @param newPotentialDirections
   * @param newFinalDirections
   * @param newPotentialPositionsForTiles
   * @return whether it was possible to make a guess or not
   */
  private static Guess guess(PotentialDirections[][] newPotentialDirections, char[][] newFinalDirections, PositionList[][] newPotentialPositionsForTiles)
  {
    PositionList positionListLength2 = null;
    PositionList lastPositionListLength3orMore = null;
    out: for (int row = 0; row < newPotentialPositionsForTiles.length; row++)
    {
      for (int column = 0; column < newPotentialPositionsForTiles[row].length; column++)
      {
        PositionList positionList = newPotentialPositionsForTiles[row][column];
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
    Guess guess = new Guess(position, newPotentialDirections, newFinalDirections);
    return guess;
  }

  private static void copy(PotentialDirections[][] potentialDirections, PotentialDirections[][] newPotentialDirections, char[][] finalDirections,
      char[][] newFinalDirections, PositionList[][] potentialPositionsForTiles, PositionList[][] newPotentialPositionsForTiles)
  {
    assert newPotentialDirections != null;
    assert potentialDirections.length == newPotentialDirections.length;
    for (int i = 0; i < potentialDirections.length; i++)
    {
      assert potentialDirections[i].length == newPotentialDirections[i].length;
      for (int j = 0; j < potentialDirections[i].length; j++)
      {
        newPotentialDirections[i][j] = (PotentialDirections) potentialDirections[i][j].clone();
      }
    }

    for (int i = 0; i < finalDirections.length; i++)
    {
      for (int j = 0; j < finalDirections[i].length; j++)
      {
        newFinalDirections[i][j] = finalDirections[i][j];
      }
    }

    for (int i = 0; i < potentialPositionsForTiles.length; i++)
    {
      for (int j = i; j < potentialPositionsForTiles[i].length; j++)
      {
        newPotentialPositionsForTiles[i][j] = (PositionList) potentialPositionsForTiles[i][j].clone();
      }
    }

  }

  private static SolveStatus isDone(PotentialDirections[][] potentialDirections, char[][] finalDirections, PositionList[][] potentialPositionsForTiles)
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

  private static boolean valid(int row, int column)
  {
    if (row < 0 || column < 0) return false;
    if (row >= numRows || column >= numColumns) return false;
    return true;
  }

  static int[] parse(String inputLine, InputModeEnum mode, int length)
  {
    int[] outputLine = new int[length];
    char c = ' ';
    String tempString = "";
    int num = -1;
    switch (mode)
    {
      case DEFAULT:
        // number of characters = number of numbers/fields in that line (=row)
        if (inputLine.length() != length)
        {
          System.out.println("The line has length " + inputLine.length() + " but you defined " + length + " columns");
        }
        assert outputLine.length == inputLine.length();
        for (int i = 0; i < inputLine.length(); i++)
        {
          c = inputLine.charAt(i);
          tempString = "" + c;
          num = Integer.parseInt(tempString);
          outputLine[i] = num;
        }
        break;
      case SPACES:
        String[] outputStringArray = inputLine.split("\\s+");
        if (outputStringArray.length != length)
        {
          System.out.println("The line has length " + outputStringArray.length + " but you defined " + length + " columns");
        }
        for (int i = 0; i < outputStringArray.length; i++)
        {
          tempString = outputStringArray[i];
          num = Integer.parseInt(tempString);
          outputLine[i] = num;
        }
        break;
      case LETTERS:
        if (inputLine.length() != length)
        {
          System.out.println("The line has length " + inputLine.length() + " but you defined " + length + " columns");
        }
        assert outputLine.length == inputLine.length();
        for (int i = 0; i < inputLine.length(); i++)
        {
          c = inputLine.charAt(i);
          if ('a' <= c && c <= 'z')
          {
            num = c - 'a' + 10;
          }
          else if ('A' <= c && c <= 'Z')
          {
            num = c - 'A' + 10;
          }
          else
          {
            tempString = "" + c;
            num = Integer.parseInt(tempString);
          }
          outputLine[i] = num;
        }
        break;
      case ZEROES:
        if (inputLine.length() != 2 * length)
        {
          System.out.println("The line has length " + inputLine.length() + " but you defined " + length + " columns, so it should be " + (2 * length));
        }
        for (int i = 0, j = 0; i < length; i++, j++)
        {
          c = inputLine.charAt(j);
          tempString = "" + c;
          j++;
          c = inputLine.charAt(j);
          tempString = tempString + c;
          num = Integer.parseInt(tempString);
          outputLine[i] = num;
        }
        break;
    }
    return outputLine;

  }

  private static String print(int[] line, boolean leadingZeroes)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < line.length; i++)
    {
      sb.append(leadingZeroes(line[i], leadingZeroes)).append(" ");
    }
    return sb.toString();
  }

  private static String leadingZeroes(int number, boolean leadingZeroes)
  {
    if (leadingZeroes == false) return "" + number;
    if (number <= 9) return "0" + number;
    else return "" + number;
  }

  private static void removeHorizontalTile(int[][] matrix, PositionList[][] potentialPositionsForTiles, int r, int c) throws UnsolvableException
  {
    int numberOnLeftTile = matrix[r][c];
    int numberOnRightTile = matrix[r][c + 1];
    if (numberOnLeftTile <= numberOnRightTile)
      potentialPositionsForTiles[numberOnLeftTile][numberOnRightTile].remove(new Position(r, c, Orientation.HORIZONTAL));
    else potentialPositionsForTiles[numberOnRightTile][numberOnLeftTile].remove(new Position(r, c, Orientation.HORIZONTAL));
  }

  private static void removeVerticalTile(int[][] matrix, PositionList[][] potentialPositionsForTiles, int r, int c) throws UnsolvableException
  {
    int numberOnTopTile = matrix[r][c];
    int numberOnBottomTile = matrix[r + 1][c];
    if (numberOnTopTile <= numberOnBottomTile) potentialPositionsForTiles[numberOnTopTile][numberOnBottomTile].remove(new Position(r, c, Orientation.VERTICAL));
    else potentialPositionsForTiles[numberOnBottomTile][numberOnTopTile].remove(new Position(r, c, Orientation.VERTICAL));
  }

  private static LinkedList<Position> setUniqueCoordinate(PositionList[][] potentialPositionsForTiles, int thisNumber, int otherNumber, int top, int left,
      Orientation dir)
  {
    int smallerNumber = -1, biggerNumber = -1;
    if (thisNumber <= otherNumber)
    {
      smallerNumber = thisNumber;
      biggerNumber = otherNumber;
    }
    else
    {
      smallerNumber = otherNumber;
      biggerNumber = thisNumber;
    }
    return potentialPositionsForTiles[smallerNumber][biggerNumber].setUniqueCoordinate(new Position(top, left, dir));
  }

}
