public class PotentialDirections implements Cloneable
{

  boolean left = true;

  boolean right = true;

  boolean up = true;

  boolean down = true;

  boolean done = false;

  @Override public PotentialDirections clone()
  {
    PotentialDirections p = new PotentialDirections();
    p.left = this.left;
    p.right = this.right;
    p.up = this.up;
    p.down = this.down;
    p.done = this.done;
    return p;
  }

  @Override public String toString()
  {
    String ret = "";
    ret += left ? DirectionEnum.LEFT.value() : ' ';
    ret += up ? DirectionEnum.UP.value() : ' ';
    ret += down ? DirectionEnum.DOWN.value() : ' ';
    ret += right ? DirectionEnum.RIGHT.value() : ' ';
    return ret;
  }

  public static void printPotentialDirections(PotentialDirections[][] potentialDirections)
  {
    for (int i = 0; i < potentialDirections.length; i++)
    {
      System.out.println(printLine(potentialDirections[i]));
    }
  }

  public static String printLine(PotentialDirections[] line)
  {
    String ret = "";
    for (int i = 0; i < line.length; i++)
    {
      ret += line[i].toString();
      ret += "|";
    }
    return ret;
  }

  public static void printFinalDirections(char[][] finalDirections)
  {
    for (int i = 0; i < finalDirections.length; i++)
    {
      if (i<10) System.out.print(" ");
      System.out.print(i+" ");
      for (int j = 0; j < finalDirections[i].length; j++)
      {
        System.out.print(finalDirections[i][j]);
      }
      System.out.println(" "+i);
    }

  }

  public static void initialize(PotentialDirections[][] matrix)
  {
    for (int r = 0; r < matrix.length; r++)
    {
      PotentialDirections[] line = matrix[r];
      for (int c = 0; c < line.length; c++)
      {
        line[c] = new PotentialDirections();
      }

    }
  }

  public void clearAllExceptRight()
  {
    up = down = left = false;
    done = true;
  }

  public void clearAllExceptLeft()
  {
    up = down = right = false;
    done = true;
  }

  public void clearAllExceptDown()
  {
    up = left = right = false;
    done = true;
  }

  public void clearAllExceptUp()
  {
    down = left = right = false;
    done = true;
  }

  public void clearRight() throws UnsolvableException
  {
    right = false;
    if (hasNoDirections()) throw new UnsolvableException();
  }

  public void clearLeft() throws UnsolvableException
  {
    left = false;
    if (hasNoDirections()) throw new UnsolvableException();
  }

  public void clearDown() throws UnsolvableException
  {
    down = false;
    if (hasNoDirections()) throw new UnsolvableException();
  }

  public void clearUp() throws UnsolvableException
  {
    up = false;
    if (hasNoDirections()) throw new UnsolvableException();
  }

  public boolean hasOnlyOneDirection()
  {
    if (num(up) + num(down) + num(right) + num(left) == 1)
    {
      return true;
    }
    return false;
  }

  public char oneDirection()
  {
    assert hasOnlyOneDirection();
    if (left) return DirectionEnum.LEFT.value();
    if (right) return DirectionEnum.RIGHT.value();
    if (up) return DirectionEnum.UP.value();
    if (down) return DirectionEnum.DOWN.value();
    assert false;
    return ' ';
  }

  public boolean hasNoDirections()
  {
    if (!left && !right && !up && !down)
    {
      return true;
    }
    return false;
  }

  private static int num(boolean b)
  {
    if (b) return 1;
    return 0;
  }

}
