
object Q8GA {

  val size = 50
   
  def fitness(cols:Array[Int]):Int = {
    var score = 0
    
    for (i <- 0 until cols.length) {
      for (j <- 0 until cols.length) {
        if (i != j) {
          if (cols(i) != cols(j) && Math.abs(i-j) - Math.abs(cols(i) - cols(j)) != 0) {
            score += 1
          }
        }
      }
    }
    return score / 2
  }
  
  /**
   *  Calculate the scores at a some given state
   */
  def fitness2(cols:Array[Int]):Int = {
    var score = 0
    val zipped = cols.zipWithIndex

    // Check horizontal 
    zipped.foreach {
      case (x, i) => {
        var collisions = cols.filter(test => x == test).length - 1
        score += collisions
      }
    }
    
    // Check diagonal
    zipped.foreach {
      case (x, idx) => {

        for (test <- 0 until 8) {
          var h = Math.abs(idx - test)
          var v = Math.abs(x - cols(test))
          
          if (h == v && v != 0) {
            score += 1
          }
        }

      }
    }
    return 56 - score
  }
  

  /**
   * Generate a random 8-Queen state
   */
  def generate():Array[Int] = {
    // Smarter generation
    var array = Array(0, 1, 2, 3, 4, 5, 6, 7)
    scala.util.Random.shuffle(array.toList).toArray
    
    
    // Complete random generation
    /*
    val rng = scala.util.Random
    return new Array[Int](8).map(f => rng.nextInt(8))
    */
  }
  

  def initialPopulation():Array[Array[Int]] = {
    var r = new Array[Array[Int]](size)
    for (i <- 0 until size) {
      r(i) = generate() 
    }
    return r
  }
  

  /**
   * Pick a random candidate based on fitness
   */
  def selection(population: Array[Array[Int]]): Array[Int] = {
    var total = 0;
    var temp = population.map( p => {
      var fit = fitness(p)

      total += fit
      (p, fit)
    })
    
    temp = temp.sortBy(_._2)
    
    // Build distribution and return single state
    val rng = scala.util.Random
    var r = rng.nextInt(total)
    var s = 0
    
    for (i <- 0 until temp.length) {
      s += temp(i)._2   
      if (r <= s) return temp(i)._1
    }

    return Array(0)
  }
  
  
  def reproduce(x:Array[Int], y:Array[Int]):Array[Int] = {
    val rng = scala.util.Random
    val n = rng.nextInt(8)
    var offspring = new Array[Int](8)
    
    for (i <- 0 until n) {
      offspring(i) = x(i)
    }
    for (i <- n until 8) {
      offspring(i) = y(i)
    }
    return offspring 
  }

  
  def pp(state:Array[Int]):String = {
    return state.mkString(",") + " fitness=" + fitness(state)
  }
  

  def resolved(population:Array[Array[Int]]):Boolean = {
    if ( population.filter( state => fitness(state) == 28).length > 0) return true
    false
  }
  
  def isSame(x:Array[Int], y:Array[Int]):Boolean = {
    if (x.length != y.length) return false
    
    for (i <- 0 until x.length) {
      if (x(i) != y(i)) return false
    }
    return true
  }
  
  def inPopulation(test:Array[Int], population:Array[Array[Int]]):Boolean = {
    
    for (i <- 0 until population.length) {
      if (population(i) != null && isSame(test, population(i)) == true) return true
    }
    
    return false;
  }



  def main(args: Array[String]) {
    println("Q8GA - Driver")
    val rng = scala.util.Random
    
    var population = initialPopulation()
    var iter = 0

    while (iter < 2000 && resolved(population) == false) {
      iter += 1
      println("==== Iteration " + iter + " ====")
      population.foreach(p => {
        // println("Population " + pp(p))
      })

      var newPopulation = new Array[Array[Int]](size)
      for (i <- 0 until size) {
        
        var exist = true;
        while (exist == true) {
          var c1 = selection(population)
          var c2 = selection(population)
          //println("\t" + pp(c1) + "\t" + pp(c2))
    
          var offspring = reproduce(c1, c2)
          var mutation = rng.nextInt(20) 
          if (mutation < 5) {
            offspring( rng.nextInt(8) ) = rng.nextInt(8)
          }

          //println("\t --> " + pp(offspring))
          if (inPopulation(offspring, newPopulation) == false) {
            exist = false
            newPopulation(i) = offspring
          }
        }
      }
      population = newPopulation
    }
    println("Resolved", resolved(population))
    if (resolved(population)) {
      println("*********************************")
      population.foreach(p => {
        println("Population " + pp(p))
      })      
    }

  }

}


//	 --> 1,4,6,3,0,7,5,2 fitness=28
