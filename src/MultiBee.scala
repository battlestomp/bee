/**
  * Created by 廷羲 on 2016/4/23.
  */

import jmetal.core._
import jmetal.util.PseudoRandom
import jmetal.util.wrapper.XReal
import jmetal.util.NonDominatedSolutionList
import jmetal.util.comparators.DominanceComparator

class Source(nfoods:Int, mlimit:Int){
  var NumFoodSources = nfoods
  var MaxLimit = mlimit
  var LimitSources = new Array[Int](NumFoodSources)
  for (i <- LimitSources.indices) LimitSources(i) = 0
  var Sources:SolutionSet = new SolutionSet(NumFoodSources)
  var SourcesProbability = new Array[Double](NumFoodSources)
  for (i <- LimitSources.indices) SourcesProbability(i) = 0
  var SourcesDominations = new Array[Int](NumFoodSources)        //支配其它解数量
  for (i <- 0 until nfoods) SourcesDominations(i) = 0
  var SourcesDominationeds = new Array[Int](NumFoodSources)        //被其它解支配解数量
  val MaxDominated = 5;
  for (i <- 0 until nfoods) SourcesDominationeds(i) = 0
  var SourcesFitness = new Array[Double](NumFoodSources)
}


class MultiBee(mproblem:Problem, nfoods:Int, mlimit:Int, iter:Int) extends Algorithm(mproblem:Problem){
  var Food:Source = new Source(nfoods, mlimit)
  val Iterations = iter
  //var NoDominations:NonDominatedSolutionList = new NonDominatedSolutionList()
  var MultiProblem:Problem = mproblem
  val DominaceCompare:DominanceComparator = new DominanceComparator()

  def initialize(): Unit ={
    for (i <- 0 until nfoods){
      var newsolution:Solution = new Solution(MultiProblem)
      MultiProblem.evaluate(newsolution)
      Food.Sources.add(newsolution)
    }
  }

  def SendEmployedBees(): Unit ={
    for (i <- 0 until nfoods) {
      Update(i)
    }
  }
  def SendOnlookerBees(): Unit ={
    caculatefitness()
    for (i <- 0 until nfoods){
      if (PseudoRandom.randDouble()<=Food.SourcesProbability(i)){
        Update(i)
      }
    }
  }

  def caculatefitness(): Unit ={
    for (i <- 0 until nfoods) Food.SourcesDominations(i) = 0
    for (i <- 0 until nfoods) Food.SourcesDominationeds(i) = 0

    for (i <- 0 until nfoods){
      for (j <- 0 until nfoods){
        var flagDominate = DominaceCompare.compare(Food.Sources.get(i), Food.Sources.get(j))
        if (flagDominate == -1)
          Food.SourcesDominations(i) = Food.SourcesDominations(i) + 1
        else if (flagDominate == 1)
          Food.SourcesDominationeds(i) =  Food.SourcesDominationeds(i) + 1

      }
    }
    var SourcesFitness = new Array[Double](nfoods)
    for(i<-SourcesFitness.indices) SourcesFitness(i) = 0
    var TotalFitness:Double = 0
    for (i <- 0 until nfoods){

      SourcesFitness(i) = Food.SourcesDominations(i).toDouble/Food.NumFoodSources.toDouble
      TotalFitness = TotalFitness + SourcesFitness(i)
    }
    for (i <- 0 until nfoods){
      Food.SourcesProbability(i) = SourcesFitness(i)/TotalFitness
    }
  }

  def Update(i:Int): Unit ={
    var j: Int = 0
    while ((j = PseudoRandom.randInt(0, nfoods-1)) == i) {}
    var cursolution = Food.Sources.get(i)
    var nextsolution = Food.Sources.get(j)
    var newsolution = UpdateSolution(cursolution, nextsolution)
    MultiProblem.evaluate(newsolution)
    var flagDominate = DominaceCompare.compare(newsolution, cursolution)
    if (flagDominate == -1) {
      //newsolution dominate cursolution
      Food.Sources.replace(i, newsolution)
      Food.LimitSources(i) = 0
    } else {
      Food.LimitSources(i) = Food.LimitSources(i) + 1
    }
    //NoDominations.add(newsolution)
  }
  def UpdateSolution(cur:Solution, next:Solution): Solution ={
    var newsolution:Solution = new Solution(cur)
    var position = PseudoRandom.randInt(0, newsolution.numberOfVariables()-1)
    var xcur:XReal = new XReal(newsolution)
    var xnext:XReal = new XReal(next)
    var pvalue = xcur.getValue(position) + PseudoRandom.randDouble(-1, 1)*(xcur.getValue(position) - xnext.getValue(position))
    if (pvalue<xcur.getLowerBound(position))
      pvalue = xcur.getLowerBound(position)
    if (pvalue>xcur.getUpperBound(position))
      pvalue = xcur.getUpperBound(position)
    xcur.setValue(position, pvalue)
    return newsolution
  }
  def SendScoutBees(): Unit ={
    for (i <- 0 until nfoods){
      if ((Food.LimitSources(i)>Food.MaxLimit) && (Food.SourcesDominationeds(i)>Food.MaxDominated) ){
        val next:Solution = new Solution(MultiProblem)
        val newsolution = UpdateSolution( Food.Sources.get(i), next);
        MultiProblem.evaluate(newsolution)
        Food.Sources.replace(i, newsolution)
      }
    }
  }
  override def execute() :SolutionSet={
    initialize()
    for (i <-0 until Iterations){
      SendEmployedBees()
      SendOnlookerBees()
      SendScoutBees()
    }
    return Food.Sources
  }
  def main(args: Array[String]) {

  }
}
