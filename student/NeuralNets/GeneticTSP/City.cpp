

class City
{
private:
	int x;
	int y;
public:
  City()
  {
    x = rand(0);
    y = rand(0);
  }
  
  char * operator(char *)()
  {
      return "(" + @x.to_s() + ", " + @y.to_s() + ")"
  }
  
  double distance(City &c)
  {
    double dx = c.x - x;
    double dy = c.y - y;
    double sum = dx * dx + dy * dy;
    sum = Math.sqrt(sum);
    return sum;
  }
};


