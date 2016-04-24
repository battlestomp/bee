/**
  * Created by 廷羲 on 2016/4/24.
  */

import jmetal.core.SolutionSet
import jmetal.problems.cec2009Competition._

object MultiBee_main {
  def main(args: Array[String]) {
    val multiProblem:UF1 = new UF1("Real")
    var NumFoodSources:Int = 100
    var MaxLimit:Int = 10
    val Iterations:Int = 250
    var algorithm:MultiBee = new MultiBee(multiProblem, NumFoodSources, MaxLimit, Iterations)
    val initTime1 = System.currentTimeMillis();
    var population:SolutionSet = algorithm.execute();
    val estimatedTime = System.currentTimeMillis() - initTime1;

  }
}
