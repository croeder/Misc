package binaryhopfield;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Image implements Cloneable{
  static  int xMax = 10;
  static  int yMax = 12;
  static int size = 10;
  boolean bits[][];

  public Object clone() throws java.lang.CloneNotSupportedException
  {
    Image n=new Image();
      for (int x=0; x<xMax; x++)
        for (int y=0; y<yMax; y++)
          n.bits[x][y] = bits[x][y];
    return n;
  }
  static void setSmall(){
    xMax = 10;
    yMax =12;
    size = 10;
  }
  static void setLarge(){
    xMax = 20;
    yMax =24;
    size = 5;
  }

  void init1() {
    for (int x=0; x<xMax; x++)
      for (int y=0; y<yMax; y++)
        bits[x][y]=false;

    for (int i=0; i<yMax; i++)
      bits[xMax/2][i] = true;

    for (int i=0; i< 0.65*xMax/2; i++)
      bits[xMax/2 - i][0 + i]=true;

    for (int i=xMax/4; i<3*xMax/4; i++)
      bits [i][yMax-1] = true;
  }

  void init0(){
    for (int x=0; x<xMax; x++)
      for (int y=0; y<yMax; y++)
        bits[x][y]=false;
    float angle=0.0f;
    int r = xMax / 2;
    for (int i=0; i<100; i++) {
      angle += Math.PI * 2 / 100;
      int x = (int) (r * Math.sin(angle) + r);
      int y = (int) (r * Math.cos(angle) + r) +1;
      bits[x][y] = true;
    }
  }

  void init3(){
  for (int x=0; x<xMax; x++)
    for (int y=0; y<yMax; y++)
      bits[x][y]=false;


  float angle=0.0f;
  int r = (int)(xMax / 3f);

  for (int i=5; i<50; i++) {
    angle = (float) (i * Math.PI * 2 / 100);
    int x = (int) (r * Math.sin(angle) + r/2);
    int y = (int) (r * Math.cos(angle) + r);
    bits[x][y] = true;
  }

  for (int i=5; i<50; i++) {
    angle = (float) (i * Math.PI * 2 / 100);
    int x = (int) (r * Math.sin(angle) + r/2);
    int y = (int) (r * Math.cos(angle) + 3*r -1);
    bits[x][y] = true;
  }
}
  void init2(){
    for (int x = 0; x < xMax; x++)
      for (int y = 0; y < yMax; y++)
        bits[x][y] = false;
    float angle = 0.0f;
    int r = xMax / 2;

    for (float i = 20.0f; i < 75.0f; i+=1.0f) {
      angle = i*(float)(Math.PI) * 2.0f / 100.0f;
      int x = (int) (r * Math.sin(angle) + r);
      int y = (int) (r * Math.cos(angle) + r);
      bits[x][y] = true;
    }

    for (int x=0; x<xMax; x++)
    {
      bits[x][xMax - x/2] = true;
    }

    for (int x = 0; x < xMax; x++)
      bits[x][yMax-1]=true;
  }

  void initSegHorz(int i)
  {
    int yStart = 0;
    switch(i) {
      case 0:
        yStart = 0;
        break;
      case 1:
        yStart = yMax/2;
        break;
      case 2:
        yStart = yMax-1;
        break;
    }

    for (int x=0; x<xMax/2 +1; x++)
    {
      bits[x][yStart]=true;
    }
  }
  void initSegHorzFat(int i)
  {
    int yStart = 0;
    int yEnd = 0;
    switch(i) {
      case 0:
        yStart = 0;
        yEnd = yMax/4;
        break;
      case 1:
        yStart = yMax/2 - yMax/8;
        yEnd = yMax/2 + yMax/8;
        break;
      case 2:
        yStart = 6*yMax/8;
        yEnd = yMax;
        break;
    }

    for (int x=0; x<xMax; x++)
    {
      for (int y=yStart; y < yEnd; y++)
        bits[x][y]=true;
    }
  }

  void init(int i)
  {
    for (int x=0; x<xMax; x++)
      for (int y=0; y<yMax; y++)
        bits[x][y]=false;

    switch (i){
      case 10:
        initSegHorz(0); initSegHorz(2);
        initSegVert(true, true); initSegVert(false, false);
        initSegVert(true, false); initSegVert(false, true);
        break;
      case 11:
        initSegVert(true, true); initSegVert(true, false);
        break;
      case 12:
        initSegVert(false, true);
        initSegVert(true, false);
         initSegHorz(0); initSegHorz(1); initSegHorz(2);
        break;
      case 13:
        initSegHorz(0); initSegHorz(1); initSegHorz(2);
        initSegVert(false, true); initSegVert(false, false);
        break;
      case 20:
        initSegVert(true, true); initSegVert(true, false);
        break;
      case 21:
        initSegHorz(0);
        break;
      case 22:
        initSegHorz(1);
        break;
      case 23:
        initSegHorz(2);
        break;
      case 30:
        initSegHorzFat(0); initSegHorzFat(2);
        initSegVertFat(true, true); initSegVertFat(false, false);
        initSegVertFat(true, false); initSegVertFat(false, true);
        break;
      case 31:
        initSegVertFat(true, true); initSegVertFat(true, false);
        break;
      case 32:
        initSegVertFat(false, true);
        initSegVertFat(true, false);
        initSegHorzFat(0); initSegHorzFat(1); initSegHorzFat(2);
        break;
      case 33:
        initSegHorzFat(0); initSegHorzFat(1); initSegHorzFat(2);
        initSegVertFat(false, true); initSegVertFat(false, false);
        break;

      default:
        System.out.println("don't know that number...");
    }
  }

  void initSegVert(boolean left, boolean top)
  {
      int height = yMax/2;
      int width = 1;
      int xStart = 0;
      int yStart = 0;

      if (!left)
        xStart += xMax/2;
      if (!top)
        yStart += yMax/2;

      for (int y=yStart + 1; y<yStart + yMax/2; y++)
      {
        bits[xStart][y] = true;
      }
  }

  void initSegVertFat(boolean left, boolean top)
  {
      int height = yMax/2;
      int width = 1;
      int xStart = 0;
      int xEnd = 0;
      int yStart = 0;

      if (!left) {
        xStart = 3 * xMax/ 4;
        xEnd = xMax;
      } else {
        xEnd = xMax/4 + 1;
        xStart = 0;
      }
      if (!top)
        yStart += yMax/2;
      for (int y=yStart + 1; y<yStart + yMax/2; y++)
      {
        for (int x=xStart; x<xEnd; x++)
          bits[x][y] = true;
      }
  }

  void addNoise(int i){
    for (int x=0; x<xMax; x++)
      for (int y=0; y<yMax; y++){
        if (Math.random() * 100 < i)
        bits[x][y] = !bits[x][y];
      }

  }

  void draw(java.awt.Graphics g, int xoff, int yoff) {
    g.drawRect(xoff, yoff, size*xMax, size*yMax);
    for (int x=0; x<xMax; x++)
      for (int y=0; y<yMax; y++)
        if (bits[x][y])
          g.fillRect(x*size + xoff, y*size + yoff, size, size);
  }

  public Image(int i) {
    bits = new boolean[xMax][yMax];
    switch(i)
    {
    case 0: init0(); break;
    case 1: init1(); break;
    case 2: init2(); break;
    case 3: init3(); break;
    case 10: // flow-through
    case 11:
    case 12:
    case 13:
    case 20:
    case 21:
    case 22:
    case 23:
    case 30:
    case 31:
    case 32:
    case 33:
      init(i); break;

    }
  }

  public Image() {
    bits = new boolean[xMax][yMax];
    for (int x=0; x<xMax; x++)
      for (int y=0; y<yMax; y++)
        bits[x][y]=false;
  }

  public int NumBitsDifferent(Image i) {
    int n=0;
      for (int x = 0; x < Image.xMax; x++) {
        for (int y = 0; y < Image.yMax; y++) {
          if  (bits[x][y] != i.bits[x][y])
            n++;
        }
      }

    return n;
  }

  public int NumBitsDifferent(int values[][]) {
    int n=0;
     for (int x = 0; x < Image.xMax; x++) {
       for (int y = 0; y < Image.yMax; y++) {
         if  ((bits[x][y] && values[x][y] == 1) ||
              (!bits[x][y] && values[x][y] > 0))
           n++;
       }
     }
     return n;
  }

}
