public class Guess
{
  private Position position;

  /**
   * 
   * @param position the guessed position
   * @param potentialDirections the new/temporary ones
   * @param finalDirections the new/temporary ones
   */
  public Guess(Position position, PotentialDirections[][] potentialDirections, char[][] finalDirections)
  {
    this.position = position;
    
    // no need to save the potential directions before 

    if (position.orientation == OrientationEnum.HORIZONTAL)
    {
      potentialDirections[position.row][position.column].clearAllExceptRight();
      finalDirections[position.row][position.column] = '>';
      potentialDirections[position.getSecondRow()][position.getSecondColumn()].clearAllExceptLeft();
      finalDirections[position.getSecondRow()][position.getSecondColumn()]  = '<';
    }
    else if (position.orientation == OrientationEnum.VERTICAL)
    {
      potentialDirections[position.row][position.column].clearAllExceptDown();
      finalDirections[position.row][position.column] = 'V';
      potentialDirections[position.getSecondRow()][position.getSecondColumn()].clearAllExceptUp();
      finalDirections[position.getSecondRow()][position.getSecondColumn()]  = '^';
    }
  }

  /**
   * 
   * @param potentialDirections the old/original
   */
  public void markGuessImpossible(PotentialDirections[][] potentialDirections)
  {
    try
    {
      if (position.orientation == OrientationEnum.HORIZONTAL)
      {
        potentialDirections[position.row][position.column].clearRight();
        potentialDirections[position.getSecondRow()][position.getSecondColumn()].clearLeft();
      }
      else if (position.orientation == OrientationEnum.VERTICAL)
      {
        potentialDirections[position.row][position.column].clearDown();
        potentialDirections[position.getSecondRow()][position.getSecondColumn()].clearUp();
      }
    }
    catch (UnsolvableException ex)
    {
      System.out.println("This shouldn't happen, when we mark a guess as impossible there can't be zero directions left in this location");
    }
  }
  
  @Override public String toString()
  {
    return position.toString();
  }

}
