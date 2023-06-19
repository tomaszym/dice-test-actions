package games.datastrophic
import org.scalacheck.Prop._
import org.scalacheck._

class DiceTest extends Properties("ChancesConfig") {

  property("for any positive n rollInt 0 <= result < n ") = forAll { (seed: Long, limit: Int) =>
    (limit > 0) ==> {
      val result = Dice.fromSeed(seed).rollInt(limit)._2

      (result >= 0) && (result < limit)
    }
  }

  property ("rollDouble 0 <= result <= 1 ") = forAll { (seed: Long) =>

    val result = Dice.fromSeed(seed).rollDouble._2

    (result >= 0) && (result <= 1)
  }

  property("observe normal distirbution's standard deviation") = forAll { (seed: Long, expectedValue: Int, stdDev: Int) =>

    (stdDev > 0) ==> {
//      val result = LazyList.unfold(Dice(seed))(d => Some(d.rollSingleNormal(expectedValue, stdDev).swap))
      val result = LazyList.iterate(Dice(seed).rollSingleNormal(stdDev,expectedValue))(d => (d._1.rollSingleNormal(stdDev, expectedValue)))

      val stats = breeze.stats.meanAndVariance(result.take(100000).map(_._2))

      (math.abs(stats.stdDev - stdDev) <= stdDev * 0.01) :| "actual std dev close enough" &&
      (math.abs(stats.mean - expectedValue) <= stdDev * 0.01) :| "actual expected value close enough"
    }
  }




}