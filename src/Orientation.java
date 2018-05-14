
public enum Orientation
{
  HORIZONTAL('H', "horizontally"), VERTICAL('V', "vertically");
  
  private char c;
  private String adverb;
  
  private Orientation(char c, String adverb)
  {
    this.c = c;
    this.adverb = adverb;
  }

  public String getSymbol()
  {
    return ""+c;
  }
  
  public String getAdverb()
  {
    return adverb;
  }
}
