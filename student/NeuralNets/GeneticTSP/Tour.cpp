#include <vector>

class Tour
{
private:
	vector<int> stops;
	vector<City> citites;

public:
  bool operator<(Tour &anOther)
    return length() < anOther.length();
  }
  
  Tour(int size)
  {
	  // stops.size=???
      for (int i=0; i<size; i++)
	  {
        stops[i] = i;
      }
      for (int i=0; i<20; i++)
	  {
        int temp = stops[i];
        r = rand(20);
        stops[i] = stops[r];
        stops[r] = temp;
      }
  }
    
 /* def to_s()
    #temp = length().to_s()
    temp = ""
    for i in 0..19
      temp += @stops[i].to_s
      temp += " "
    }
    return temp
  }*/
  
  double length()  
    double dist = 0.0;
    for (int i=0; i<20; i++)
	{
      dist += cities[stops[i]].distance(cities[stops[i+1]]);
    }
    dist += cities[stops[19]].distance(cities[stops[0]]);
    return dist;
  }
  
  void setStop(int index, int value)
  {
    stops[index]=value;
  }
  
  int getStop(int index)
  {
    return stops[index];
  } 
  
  void breed(Tour &father)
  {
    cross1 = rand(20);
    cross2 = rand(20);

    Tour child;
    if (cross1 > cross2)
      temp = cross1
      cross1 = cross2
      cross2 = temp
    }
    # copy from "mother"
    for (int i=cross1; in cross1..cross2-1
      child.setStop(i-cross1, @stops[i])
    }
    
    # copy from "father" yukkkkkkkkkkkkkkkkkkk
    # start with a copy of the father array
    fatherCopy = Tour.new
    for i in 0..19
      fatherCopy.setStop(i, father.getStop(i))
    }
    # first, remove items from fatherCopy that the mother's section has...change them to -1
    for i in cross1..cross2-1
      for j in 0..19
          if (fatherCopy.getStop(j) == @stops[i])
            fatherCopy.setStop(j,-1);
          }
      }
    }
    # now copy non -1 values from copyFather to child
    childIndex=cross2-cross1 
    for i in 0..19
      if (fatherCopy.getStop(i) != -1)
          child.setStop(childIndex, fatherCopy.getStop(i))
          childIndex += 1
      }
    }
    
    return child
  }
  
  def mutate1()
    # swap 2 stops
    one = rand(20)
    two = rand(20)
    temp = @stops[one]
    @stops[one] = @stops[two]
    @stops[two] = temp
  }
  
  def mutate2()
    # invert a range
    one = rand(20)
    two = rand(20)
    length = (one - two).abs
    for i in 0..(length)/2
      temp = @stops[one + i]
      @stops[one + i] = @stops[one + length -i]
      @stops[one + length -i] = temp
    }
  }
}

def testBreed()
  t1 = Tour.new
  t2 = Tour.new
  kid = t1.breed(t2)
  puts t1
  puts t2
  puts kid
}

def testMutate1()
  t = Tour.new
  puts(t)
  t.mutate1()
  puts(t)
}

def testMutate2()
  t = Tour.new
  puts(t)
  t.mutate2()
  puts(t)
}

def testTour()
  testBreed()
  puts
  testMutate1()
  puts
  testMutate2()
}
