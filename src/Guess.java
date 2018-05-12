public class Guess
{
  Position position;

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

    if (position.orientation == Orientation.HORIZONTAL)
    {
      potentialDirections[position.row][position.column].clearAllExceptRight();
      finalDirections[position.row][position.column] = '>';
      potentialDirections[position.secondRow()][position.secondColumn()].clearAllExceptLeft();
      finalDirections[position.secondRow()][position.secondColumn()] = '<';
    }
    else if (position.orientation == Orientation.VERTICAL)
    {
      potentialDirections[position.row][position.column].clearAllExceptDown();
      finalDirections[position.row][position.column] = 'V';
      potentialDirections[position.secondRow()][position.secondColumn()].clearAllExceptUp();
      finalDirections[position.secondRow()][position.secondColumn()] = '^';
    }
  }

  @Override public String toString()
  {
    return position.toString();
  }

  public Position getPosition()
  {
    return position;
  }

}
