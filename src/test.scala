/**
  * Created by 廷羲 on 2016/4/23.
  */

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import jmetal.core.SolutionType
import jmetal.core.Variable
import jmetal.encodings.solutionType.RealSolutionType
import jmetal.util.JMException

object test {
  def main(args: Array[String]) {

    val limitSources = new Array[Int](5)

   for (i <- limitSources.indices) limitSources(i) = 1
    limitSources.foreach(print)
  }
}
