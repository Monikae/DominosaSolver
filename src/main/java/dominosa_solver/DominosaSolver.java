package dominosa_solver;

import static def.dom.Globals.console;

public class DominosaSolver{

  public static def.js.Array<Position> getSolution(int numRows, int numColumns, int highestNumber, int[][] numbersOnBoard) 
  {
    Board.printSteps = true; 
    
    Board board = Board.createInitialBoard(highestNumber, numRows, numColumns, numbersOnBoard);

    SolveStatus solveStatus = board.seek();
    if (solveStatus != SolveStatus.SOLVED) {
    	return null;
    } else {
        console.log("End - final directions:");
        PotentialDirections.printFinalDirections(board.finalDirections);
        return getPositions(board.potentialPositionsForTiles, highestNumber);   	
    }
  }


  private static def.js.Array<Position> getPositions(PositionList[][] solution, int highestNumber) {
	def.js.Array<Position> positions = new def.js.Array<Position>(); 

    for (int i = 0; i <= highestNumber; i++)
    {
      for (int j = i; j <= highestNumber; j++)
      {
        Position pos = solution[i][j].getFirst();
        positions.push(pos);
      }
    }
    return positions; 
  }

  private static String leadingZeroes(int number, boolean leadingZeroes)
  {
    if (leadingZeroes == false) return "" + number;
    if (number <= 9) return "0" + number;
    else return "" + number;
  }

}