import java.util.ArrayList;

public class Position
{
  // coordinates of top/left field
  final int row, column;

  Orientation orientation;

  @Override public String toString()
  {
    return row + "," + column + " " + orientation;
  }

  public Position(int row, int column, Orientation orientation)
  {
    this.row = row;
    this.column = column;
    this.orientation = orientation;
  }

  public Iterable<Position> getOverlappingPositions()
  {
    ArrayList<Position> list = new ArrayList<>();
    for (Square a : getSquaresAbove())
    {
      list.add(new Position(a.row, a.column, Orientation.VERTICAL));
    }
    for (Square l : getSquaresLeft())
    {
      list.add(new Position(l.row, l.column, Orientation.HORIZONTAL));
    }
    for (Square b : getSquaresBelow())
    {
      list.add(new Position(b.row - 1, b.column, Orientation.VERTICAL));
    }
    for (Square r : getSquaresRight())
    {
      list.add(new Position(r.row, r.column - 1, Orientation.HORIZONTAL));
    }

    return list;
  }

  public int getLowerNumber()
  {
    if (getFirstNumber() <= getSecondNumber()) return getFirstNumber();
    return getSecondNumber();
  }

  public int getHigherNumber()
  {
    if (getFirstNumber() <= getSecondNumber()) return getSecondNumber();
    return getFirstNumber();
  }

  private int getFirstNumber()
  {
    return Board.numbersOnBoard[row][column];
  }

  private int getSecondNumber()
  {
    return Board.numbersOnBoard[secondRow()][secondColumn()];
  }

  public Square[] getSquares()
  {
    Square[] squares = new Square[2];
    squares[0] = new Square(row, column);
    if (orientation == Orientation.HORIZONTAL)
    {
      squares[1] = new Square(row, column + 1);
    }
    else if (orientation == Orientation.VERTICAL)
    {
      squares[1] = new Square(row + 1, column);
    }
    return squares;
  }

  public int secondRow()
  {
    return getSquares()[1].row;
  }

  public int secondColumn()
  {
    return getSquares()[1].column;
  }

  /**
   * 
   * @return the square or squares above, if any
   */
  public Square[] getSquaresAbove()
  {
    Square[] squares = new Square[0];
    if (this.row == 0) return squares;
    if (orientation == Orientation.HORIZONTAL)
    {
      squares = new Square[2];
      squares[0] = new Square(row - 1, column);
      squares[1] = new Square(row - 1, column + 1);
    }
    else if (orientation == Orientation.VERTICAL)
    {
      squares = new Square[1];
      squares[0] = new Square(row - 1, column);
    }
    return squares;
  }

  /**
   * 
   * @return the square or squares below, if any
   */
  public Square[] getSquaresBelow()
  {
    Square[] squares = new Square[0];
    if (orientation == Orientation.HORIZONTAL)
    {
      if (this.row == Board.numRows - 1) return squares; // length 0
      squares = new Square[2];
      squares[0] = new Square(row + 1, column);
      squares[1] = new Square(row + 1, column + 1);
    }
    else if (orientation == Orientation.VERTICAL)
    {
      if (this.row == Board.numRows - 2) return squares; // length 0
      squares = new Square[1];
      squares[0] = new Square(row + 2, column);
    }
    return squares;
  }

  /**
   * 
   * @return the square or squares to the left, if any
   */
  public Square[] getSquaresLeft()
  {
    Square[] squares = new Square[0];
    if (this.column == 0) return squares;
    if (orientation == Orientation.VERTICAL)
    {
      squares = new Square[2];
      squares[0] = new Square(row, column - 1);
      squares[1] = new Square(row + 1, column - 1);
    }
    else if (orientation == Orientation.HORIZONTAL)
    {
      squares = new Square[1];
      squares[0] = new Square(row, column - 1);
    }
    return squares;
  }

  /**
   * 
   * @return the square or squares to the right, if any
   */
  public Square[] getSquaresRight()
  {
    Square[] squares = new Square[0];
    if (orientation == Orientation.VERTICAL)
    {
      if (this.column == Board.numColumns - 1) return squares; // length 0
      squares = new Square[2];
      squares[0] = new Square(row, column + 1);
      squares[1] = new Square(row + 1, column + 1);
    }
    else if (orientation == Orientation.HORIZONTAL)
    {
      if (this.column == Board.numColumns - 2) return squares; // length 0
      squares = new Square[1];
      squares[0] = new Square(row, column + 2);
    }
    return squares;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((orientation == null) ? 0 : orientation.hashCode());
    result = prime * result + row;
    result = prime * result + column;
    return result;
  }

  @Override public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Position other = (Position) obj;
    if (orientation != other.orientation) return false;
    if (row != other.row) return false;
    if (column != other.column) return false;
    return true;
  }
}
