import java.util.LinkedList;


public class PositionList implements Cloneable
{
  private LinkedList<Position> list = new LinkedList<>();
  public boolean done = false;
  
  @Override public PositionList clone()
  {
    PositionList p = new PositionList();
    p.done = this.done;
    p.list = (LinkedList<Position>) this.list.clone();
    return p;
  }
  
  public void add(Position c)
  {
    list.add(c);
  }
  public void remove(Position c) throws UnsolvableException
  {
    list.remove(c);
    if (isEmpty())
      throw new UnsolvableException();
  }
  public boolean hasExactlyOne()
  {
    return list.size() == 1;
  }
  public boolean isEmpty()
  {
    return list.size() == 0;
  }
  
  public int size()
  {
    return list.size();
  }
  
  public Position getFirst()
  {
    return list.getFirst();
  }
  
  public LinkedList<Position> setUniqueCoordinate(Position position)
  {
    LinkedList<Position> oldList = list;
    oldList.remove(position);
    
    list = new LinkedList<>();
    list.add(position);
    
    return oldList;
  }
  

}
