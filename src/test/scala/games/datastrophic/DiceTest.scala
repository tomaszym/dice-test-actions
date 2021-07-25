package games.datastrophic
import org.scalacheck.Prop._
import org.scalacheck._

class DiceTest extends Properties("ChancesConfig") {

  property("for any positive n rollInt 0 <= result < n ") = forAll { (seed: Long, limit: Int) =>
    (limit > 0) ==> {
      val result = Dice(seed).rollInt(limit)._2

      (result >= 0) && (result < limit)
    }
  }

}