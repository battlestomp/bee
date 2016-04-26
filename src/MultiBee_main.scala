/**
  * Created by 廷羲 on 2016/4/24.
  */

import jmetal.core.SolutionSet
import jmetal.problems.cec2009Competition._
import jmetal.qualityIndicator.QualityIndicator;

object MultiBee_main {
  def main(args: Array[String]) {
    val multiProblem:UF1 = new UF1("Real")
    val NumFoodSources:Int = 100
    val MaxLimit:Int = 10
    val Iterations:Int = 2500
    val algorithm:MultiBee = new MultiBee(multiProblem, NumFoodSources, MaxLimit, Iterations)
    val initTime1 = System.currentTimeMillis();
    var population:SolutionSet = algorithm.execute();
    val estimatedTime = System.currentTimeMillis() - initTime1;
    System.out.println("Total execution time: " + estimatedTime + "ms")
    val nodominatesolution = algorithm.NoDominateSolution()
    nodominatesolution.printObjectivesToFile("FUN1");
    val indicators:QualityIndicator = new QualityIndicator(multiProblem, "pf/UF1.dat");
    if(indicators != null) {
      System.out.println("Quality indicators");
      System.out.println("Hypervolume: " + indicators.getHypervolume(population));
      System.out.println("EPSILON    : " + indicators.getEpsilon(population));
      System.out.println("GD         : " + indicators.getGD(population));
      System.out.println("IGD        : " + indicators.getIGD(population));
      System.out.println("Spread     : " + indicators.getSpread(population));
    }
  }
}
