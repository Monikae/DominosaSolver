package dominosa_solver;

import java.util.function.Function;

public class PositionList 
{
  private def.js.Array<Position> list = new def.js.Array<>();

  public boolean done = false;
  
  public PositionList() {
	  
  }
  
  public PositionList (PositionList orig)
  {
    this.done = orig.done;
    this.list =  orig.list.slice();
  }

  public void add(Position c)
  {
    list.push(c);
  }
  
  private void remove(def.js.Array<Position> l, Position c)  {
	int index = this.indexOf(l, c);
	if (index > -1) {
	   l.splice(index, 1 );
	}	  
  }
  
  public void remove(Position c) throws UnsolvableException
  {
	this.remove(this.list, c);
    if (isEmpty()) throw new UnsolvableException();
  }
  
  private int indexOf(def.js.Array<Position> l, Position c) {
	Function<Position, Boolean> postionEquals =   (Position p) -> {return c.equals(p);};
		  
	int index = (int)l.findIndex(postionEquals);
    return index; 	  
  }
  
  public boolean hasExactlyOne()
  {
    return list.length == 1;
  }

  public boolean isEmpty()
  {
    return list.length == 0;
  }

  public int size()
  {
    return (int)list.length;
  }

  public Position getFirst()
  {
    return list.$get(0);
  }

  public def.js.Array<Position> setUniquePosition(Position position)
  {
	def.js.Array<Position> oldList = list;
	
    this.remove(list, position);
	
    list = new def.js.Array<>();
    list.push(position);

    return oldList;
  }

  @Override public String toString()
  {
    return (done?"done: ":"not done: ")+list.toString();
  }

}
