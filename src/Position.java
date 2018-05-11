

public class Position
{
  //coordinates of top/left field
  int row, column;
  OrientationEnum orientation;
  
  @Override public String toString()
  {
    return row + "," + column + " " + orientation;
  }
  
  public Position(int down, int right, OrientationEnum orientation)
  {
    this.row = down;
    this.column = right;
    this.orientation = orientation;
  }
  
  public Square[] getSquares()
  {
    Square[] squares = new Square[2];
    squares[0] = new Square(row, column);
    if (orientation == OrientationEnum.HORIZONTAL)
    {
      squares[1] = new Square(row, column+1);
    } else if (orientation == OrientationEnum.VERTICAL)
    {
      squares[1] = new Square(row+1, column);
    }  
    return squares;
  }
  
  public int getSecondRow()
  {
    return getSquares()[1].row;
  }
  
  public int getSecondColumn()
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
    if (orientation == OrientationEnum.HORIZONTAL)
    {
      squares = new Square[2];
      squares[0] = new Square(row-1, column);
      squares[1] = new Square(row-1, column+1);
    } else if (orientation == OrientationEnum.VERTICAL)
    {
      squares = new Square[1];
      squares[0] = new Square(row-1, column);
    }  
    return squares;    
  }
  
  /**
   * 
   * @return the square or squares below, if any
   */
  public Square[] getSquaresBelow(int numRows)
  {
    Square[] squares = new Square[0];
    if (orientation == OrientationEnum.HORIZONTAL)
    {
      if (this.row == numRows-1) return squares;
      squares = new Square[2];
      squares[0] = new Square(row+1, column);
      squares[1] = new Square(row+1, column+1);
    } else if (orientation == OrientationEnum.VERTICAL)
    {
      if (this.row == numRows-2) return squares;
      squares = new Square[1];
      squares[0] = new Square(row+2, column);
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
    if (orientation == OrientationEnum.VERTICAL)
    {
      squares = new Square[2];
      squares[0] = new Square(row, column-1);
      squares[1] = new Square(row+1, column-1);
    } else if (orientation == OrientationEnum.HORIZONTAL)
    {
      squares = new Square[1];
      squares[0] = new Square(row, column-1);
    }  
    return squares;    
  }
  
  /**
   * 
   * @return the square or squares to the right, if any
   */
  public Square[] getSquaresRight(int numColumns)
  {
    Square[] squares = new Square[0];
    if (orientation == OrientationEnum.VERTICAL)
    {
      if (this.column == numColumns-1) return squares;
      squares = new Square[2];
      squares[0] = new Square(row, column+1);
      squares[1] = new Square(row+1, column+1);
    } else if (orientation == OrientationEnum.HORIZONTAL)
    {
      if (this.column == numColumns-2) return squares;
      squares = new Square[1];
      squares[0] = new Square(row, column+2);
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
