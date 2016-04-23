/**
  * Created by 廷羲 on 2016/4/23.
  */

import jmetal.core._
import jmetal.problems.cec2009Competition._
import jmetal.util.PseudoRandom
import jmetal.util.wrapper.XReal
import java.util.Comparator
import jmetal.util.comparators.DominanceComparator

object MultiBee {

  var NumFoodSources = 100
  var LimitSources = new Array[Int](NumFoodSources)
  for (i <- 0 until LimitSources.length) LimitSources(i) = 0
  var Sources:SolutionSet = new SolutionSet(NumFoodSources)
  var MultiProblem:Problem = new UF1("ArrayReal")
  val DominaceCompare:DominanceComparator = new DominanceComparator()


  def initialize(): Unit ={
    for (i <- 0 until LimitSources.length){
      var newsolution:Solution = new Solution(MultiProblem)
      MultiProblem.evaluate(newsolution)
      MultiProblem.evaluateConstraints(newsolution)
      Sources.add(newsolution)
    }
  }

  def SendEmployedBees(): Unit ={
    for (i <- 0 until LimitSources.length) {
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
  }
  def SendOnlookerBees(): Unit ={

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
  def SendScoutBees(){

  }
  def main(args: Array[String]) {
    var LimitSources = new Array[Int](NumFoodSources)

    LimitSources.foreach(print)
    System.out.println("HelloWorld11111");
  }
}
