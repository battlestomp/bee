/**
  * Created by 廷羲 on 2016/4/23.
  */

import jmetal.core._
import jmetal.problems.cec2009Competition._
import jmetal.util.PseudoRandom
import jmetal.util.wrapper.XReal
import jmetal.util.comparators.DominanceComparator

object MultiBee {

  var NumFoodSources = 100
  var MaxLimit = 10
  var LimitSources = new Array[Int](NumFoodSources)
  for (i <- 0 until LimitSources.length) LimitSources(i) = 0
  var Sources:SolutionSet = new SolutionSet(NumFoodSources)
  var MultiProblem:Problem = new UF1("ArrayReal")
  val DominaceCompare:DominanceComparator = new DominanceComparator()
  var SourcesProbability = new Array[Double](NumFoodSources)
  for (i <- 0 until LimitSources.length) SourcesProbability(i) = 0

  def initialize(): Unit ={
    for (i <- 0 until LimitSources.length){
      var newsolution:Solution = new Solution(MultiProblem)
      MultiProblem.evaluate(newsolution)
      Sources.add(newsolution)
    }
  }

  def SendEmployedBees(): Unit ={
    for (i <- 0 until LimitSources.length) {
      Update(i)
    }
  }
  def SendOnlookerBees(): Unit ={
    caculatefitness()
    for (i <- 0 until LimitSources.length){
      if (PseudoRandom.randDouble()<=SourcesProbability(i)){
        Update(i)
      }
    }
  }

  def caculatefitness(): Unit ={
    var SourcesDominations = new Array[Int](NumFoodSources)
    for (i <- 0 until LimitSources.length){
      for (j <- 0 until LimitSources.length){
        var flagDominate = DominaceCompare.compare(Sources.get(i), Sources.get(j))
        if (flagDominate == -1)
          SourcesDominations(i) = SourcesDominations(i) + 1
      }
    }
    var SourcesFitness = new Array[Double](NumFoodSources)
    var TotalFitness:Double = 0
    for (i <- 0 until LimitSources.length){
      SourcesFitness(i) = SourcesDominations(i)/NumFoodSources
      TotalFitness = TotalFitness + SourcesFitness(i)
    }
    for (i <- 0 until LimitSources.length){
      SourcesProbability(i) = SourcesFitness(i)/TotalFitness
    }
  }

  def Update(i:Int): Unit ={
    var j: Int = 0
    while ((j = PseudoRandom.randInt(0, LimitSources.length)) == i) {}
    var cursolution = Sources.get(i)
    var nextsolution = Sources.get(j)
    var newsolution = UpdateSolution(cursolution, nextsolution)
    MultiProblem.evaluate(newsolution)
    var flagDominate = DominaceCompare.compare(newsolution, cursolution)
    if (flagDominate == -1) {
      //newsolution dominate cursolution
      Sources.replace(i, newsolution)
      LimitSources(i) = 0
    } else {
      LimitSources(i) = LimitSources(i) + 1
    }
  }
  def UpdateSolution(cur:Solution, next:Solution): Solution ={
    var newsolution:Solution = new Solution(Solution)
    var position = PseudoRandom.randInt(0, newsolution.numberOfVariables())
    var xcur:XReal = new XReal(newsolution)
    var xnext:XReal = new XReal(next)
    var pvalue = xcur.getValue(position) + PseudoRandom.randDouble(-1, 1)*(xcur.getValue(position) - xnext.getValue(position))
    if (pvalue<xcur.getLowerBound(position))
      pvalue = xcur.getLowerBound(position)
    if (pvalue<xcur.getUpperBound(position))
      pvalue = xcur.getUpperBound(position)
    xcur.setValue(position, pvalue)
    return newsolution
  }
  def SendScoutBees(): Unit ={
    for (i <- 0 until LimitSources.length){
      if (LimitSources(i)>MaxLimit){
        var newsolution:Solution = new Solution(MultiProblem)
        MultiProblem.evaluate(newsolution)
        Sources.replace(i, newsolution)
      }
    }
  }
  def main(args: Array[String]) {
    var LimitSources = new Array[Int](NumFoodSources)

    LimitSources.foreach(print)
    System.out.println("HelloWorld11111");
  }
}
