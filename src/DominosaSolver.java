import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

}
