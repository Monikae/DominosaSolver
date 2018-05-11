
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
    ret+= left  ? '<' : ' ';
    ret+= up    ? '^' : ' ';
    ret+= down  ? 'V' : ' ';
    ret+= right ? '>' : ' ';
    return ret;
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
  
  public static void initialize(PotentialDirections[][] matrix)
  {
    for (int i = 0; i < matrix.length; i++)
    {
      PotentialDirections[] line = matrix[i];
      for (int j = 0; j < line.length; j++)
      {
        line[j] = new PotentialDirections();
      }
      
    }
  }
  
  public void clearAllExceptRight()
  {
    up = down = left = false;
  }
  public void clearAllExceptLeft()
  {
    up = down = right = false;
  }
  public void clearAllExceptDown()
  {
    up = left = right = false;
  }
  public void clearAllExceptUp()
  {
    down = left = right = false;
  }  
  
  public void clearRight() throws UnsolvableException
  {
    right = false;
    if (hasNoDirections())
      throw new UnsolvableException();
  }
  public void clearLeft() throws UnsolvableException
  {
    left = false;
    if (hasNoDirections())
      throw new UnsolvableException();
  }
  public void clearDown() throws UnsolvableException
  {
    down = false;
    if (hasNoDirections())
      throw new UnsolvableException();    
  }
  public void clearUp() throws UnsolvableException
  {
    up = false;
    if (hasNoDirections())
      throw new UnsolvableException();    
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
    if (left) return '<';
    if (right) return '>';
    if (up) return '^';
    if (down) return 'V';
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
