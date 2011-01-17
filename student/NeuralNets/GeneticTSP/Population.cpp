class Population
  def initialize()
    @members = Array.new 
    for i in 0..19
      @members[i] = Tour.new
    end
    @numTours=20;
    @cities = Array.new
    for i in 0..19
      @cities[i] = City.new
    end
  end

  def print()
    puts "tours..."
    for i in 0..19
      $stdout.print(@members[i].to_s())
      puts @members[i].length(@cities)
    end
    puts "cities.."
    for i in 0..19
      $stdout.print(@cities[i].to_s(), "\n")
    end
  end
  
  def chooseFather
      return @members[rand(@numTours)]
  end
  
  def chooseMother
    return @members[rand(@numTours/2)]
  end
  
  def evolve()
    while (true)
      for i in 0..@numTours-1
        father = chooseFather()
        mother = chooseMother()
        child = mother.breed(father)
        @members[@numTours] = child
        @numTours = @numTours + 1
      end
    end
  end
end

pop = Population.new
for x in 0..100
  pop.evolve()
  pop.print()
end
