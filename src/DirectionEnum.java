
public enum DirectionEnum
{
  LEFT('<'), RIGHT('>'), UP('^'), DOWN('v'), NOT_SET('?');

  private final char depiction;

  private DirectionEnum(char value)
  {
    depiction = value;
  }

  @Override public String toString()
  {
    return "" + depiction;
  }

  public char value()
  {
    return depiction;
  }
}
