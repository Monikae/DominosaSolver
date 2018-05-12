public class DominosaSolverTest
{

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    test3();
    test4();
    // test20();

  }

  static void test3()
  {
    int numColumnsTest = 5;
    int numRowsTest = 4;
    int highestNumberTest = 3;
    int[][] linesTest = new int[numRowsTest][numColumnsTest];
    linesTest[0] = DominosaSolver.parse("02232", InputModeEnum.DEFAULT, numColumnsTest);
    linesTest[1] = DominosaSolver.parse("30011", InputModeEnum.DEFAULT, numColumnsTest);
    linesTest[2] = DominosaSolver.parse("13302", InputModeEnum.DEFAULT, numColumnsTest);
    linesTest[3] = DominosaSolver.parse("20113", InputModeEnum.DEFAULT, numColumnsTest);
    DominosaSolver.test(numColumnsTest, numRowsTest, highestNumberTest, linesTest);
  }

  static void test4()
  {
    int numColumnsTest = 6;
    int numRowsTest = 5;
    int highestNumberTest = 4;
    int[][] linesTest = new int[numRowsTest][numColumnsTest];
    linesTest[0] = DominosaSolver.parse("231210", InputModeEnum.DEFAULT, numColumnsTest);
    linesTest[1] = DominosaSolver.parse("000410", InputModeEnum.DEFAULT, numColumnsTest);
    linesTest[2] = DominosaSolver.parse("331341", InputModeEnum.DEFAULT, numColumnsTest);
    linesTest[3] = DominosaSolver.parse("224313", InputModeEnum.DEFAULT, numColumnsTest);
    linesTest[4] = DominosaSolver.parse("224404", InputModeEnum.DEFAULT, numColumnsTest);
    DominosaSolver.test(numColumnsTest, numRowsTest, highestNumberTest, linesTest);
  }

  static void test20()
  {
    int numColumnsTest = 22;
    int numRowsTest = 21;
    int highestNumberTest = 20;
    int[][] linesTest = new int[numRowsTest][numColumnsTest];
    linesTest[0] = DominosaSolver.parse("1 5 8 15 11 14 15 9 1 10 3 4 16 12 8 10 14 9 16 7 9 0", InputModeEnum.SPACES, numColumnsTest);
    linesTest[1] = DominosaSolver.parse("18 5 6 9 7 7 16 12 13 12 14 15 15 4 2 2 1 0 20 10 11 6", InputModeEnum.SPACES, numColumnsTest);
    linesTest[2] = DominosaSolver.parse("18 12 12 4 19 8 2 1 1 3 14 0 5 19 9 16 0 3 14 5 11 18", InputModeEnum.SPACES, numColumnsTest);
    linesTest[3] = DominosaSolver.parse("2 20 7 14 0 7 20 10 10 18 3 15 2 11 9 16 10 3 15 11 16 4", InputModeEnum.SPACES, numColumnsTest);
    linesTest[4] = DominosaSolver.parse("20 4 18 9 15 10 0 6 9 17 4 9 12 8 20 8 5 14 19 16 1 2", InputModeEnum.SPACES, numColumnsTest);
    linesTest[5] = DominosaSolver.parse("14 8 14 7 9 11 9 13 10 10 1 18 5 0 12 16 6 6 15 12 17 5", InputModeEnum.SPACES, numColumnsTest);
    linesTest[6] = DominosaSolver.parse("1 4 19 17 3 16 8 6 19 4 1 7 19 0 6 1 8 11 6 20 18 9", InputModeEnum.SPACES, numColumnsTest);
    linesTest[7] = DominosaSolver.parse("14 1 3 19 18 19 13 15 3 5 14 0 19 18 19 17 3 16 0 7 17 17", InputModeEnum.SPACES, numColumnsTest);
    linesTest[8] = DominosaSolver.parse("20 2 6 12 16 13 13 6 7 18 6 4 19 12 12 15 9 4 11 4 10 12", InputModeEnum.SPACES, numColumnsTest);
    linesTest[9] = DominosaSolver.parse("6 7 12 13 14 1 4 13 5 9 16 13 10 0 11 5 18 15 6 1 15 14", InputModeEnum.SPACES, numColumnsTest);
    linesTest[10] = DominosaSolver.parse("2 3 5 13 18 17 7 18 5 7 17 11 10 6 14 14 16 2 3 0 8 20", InputModeEnum.SPACES, numColumnsTest);
    linesTest[11] = DominosaSolver.parse("17 11 3 3 5 11 7 13 13 5 0 17 10 6 16 8 1 11 20 13 8 3", InputModeEnum.SPACES, numColumnsTest);
    linesTest[12] = DominosaSolver.parse("7 15 15 13 9 11 15 0 2 13 13 12 14 6 12 18 10 10 17 1 4 1", InputModeEnum.SPACES, numColumnsTest);
    linesTest[13] = DominosaSolver.parse("17 17 17 20 20 4 4 4 20 20 19 15 20 2 2 16 8 19 8 0 5 7", InputModeEnum.SPACES, numColumnsTest);
    linesTest[14] = DominosaSolver.parse("2 6 6 13 20 19 12 0 8 18 0 11 20 9 15 9 11 4 9 4 16 5", InputModeEnum.SPACES, numColumnsTest);
    linesTest[15] = DominosaSolver.parse("19 11 20 18 9 0 18 2 10 18 3 8 11 10 2 16 6 17 7 5 13 2", InputModeEnum.SPACES, numColumnsTest);
    linesTest[16] = DominosaSolver.parse("7 0 14 17 14 10 13 17 2 20 16 6 1 4 12 12 7 3 13 19 8 11", InputModeEnum.SPACES, numColumnsTest);
    linesTest[17] = DominosaSolver.parse("8 16 20 0 0 8 15 19 1 2 19 7 20 9 2 0 6 3 11 10 8 1", InputModeEnum.SPACES, numColumnsTest);
    linesTest[18] = DominosaSolver.parse("7 12 15 5 18 12 16 6 12 5 5 18 19 14 11 17 4 13 13 17 8 15", InputModeEnum.SPACES, numColumnsTest);
    linesTest[19] = DominosaSolver.parse("7 16 1 8 11 5 3 10 1 19 14 19 17 2 3 13 3 20 7 8 3 14", InputModeEnum.SPACES, numColumnsTest);
    linesTest[20] = DominosaSolver.parse("14 16 2 2 18 20 9 5 15 3 1 10 17 10 12 17 4 18 9 19 4 15", InputModeEnum.SPACES, numColumnsTest);
    DominosaSolver.test(numColumnsTest, numRowsTest, highestNumberTest, linesTest);
  }

}
