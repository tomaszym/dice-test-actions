/*
 * Copyright 2020 Tomasz Szymula
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package games.datastrophic

import scala.annotation.tailrec
import scala.util.Random

case class Dice private (state: Long) {

  private def roll32Bits: (Dice, Int) = {
    val xorShifted = (((state >>> 18) ^ state) >>> 27).toInt
    val rot = (state >>> 59).toInt

    (new Dice(state * Dice.MULTIPLIER + Dice.INCREMENT), Integer.rotateRight(xorShifted, rot | 1))
  }

  private def roll64Bits: (Dice, Long) = {
    val (d, i) = roll32Bits
    val (d2, i2) = d.roll32Bits

    val l = ((i.toLong & Dice.MASK)  << 32) | (i2.toLong & Dice.MASK)

    (d2, l)
  }

  private def rollBits(bits: Int) = {
    val (next, result) = roll32Bits
    (next, result >>> 32-bits)
  }

  def rollIntMaxValue: (Dice, Int) = roll32Bits
  def rollLongMaxValue: (Dice, Long) = roll64Bits

  def rollInt(bound: Int): (Dice, Int) = {
    val (d, i) = roll32Bits
    (d, i % bound abs)
  }
  def nextInt(bound: Int): Int = rollInt(bound)._2

  def rollDouble: (Dice, Double) = {
    val (dice, i) = roll64Bits
    (dice, (i >>> 11) * Dice.DOUBLE_UNIT)
  }

  def rollNormal(standardDeviation: Double, expectedValue: Double): (Dice, (Double, Double)) = {

    if(standardDeviation < 0) throw new IllegalArgumentException(s"standard deviation has to be nonegative, got ${standardDeviation}")

    val (diceA, a) = rollDouble

    val (diceB, b) = diceA.rollDouble

    val (x,y) = BoxMuller(a,b)

    (diceB,(x*standardDeviation + expectedValue, y*standardDeviation + expectedValue))
  }

  def rollSingleNormal(standardDeviation: Double, expectedValue: Double): (Dice, Double) = {
    val (d, (a, _)) =    rollNormal(standardDeviation, expectedValue)
    (d,a)
  }

  def nextNormal(standardDeviation: Double, expectedValue: Double): (Double, Double) = rollNormal(standardDeviation, expectedValue)._2
  def nextSingleNormal(standardDeviation: Double, expectedValue: Double): Double = nextNormal(standardDeviation, expectedValue)._1

  def nextDouble: Double = rollDouble._2

  def rollChanceMap[A](thingWithChance: Map[A, Int]): (Dice, A) = {

    val sum = thingWithChance.values.sum
    val (d, i) = rollInt(sum)

    @tailrec
    def it(left: List[(A, Int)], counter: Int): A = {
      left match {

        case Nil => ???

        case (result, threshold) :: tail =>
          if(counter <= threshold)
            result
          else
            it(tail, counter - threshold)
      }
    }

    val a = it(thingWithChance.toList, i+1)

    (d, a)
  }

  @deprecated("unsafe")
  def unsafeRollOneOf[A](ax: Seq[A]): (Dice,A) = {
    val (dice, index) = rollInt(ax.size)
    (dice, ax.apply(index))
  }
  @deprecated("unsafe")
  def unsafeOneOf[A](ax: Seq[A]): A = unsafeRollOneOf(ax)._2


  def rollOneOf[A](ax: Seq[A]): Option[(Dice,A)] = if(ax.nonEmpty) Some {
    val (dice, index) = rollInt(ax.size)
    (dice, ax.apply(index))
  } else None

  def oneOf[A](ax: Seq[A]): Option[A] = rollOneOf(ax).map(_._2)


  @deprecated("use roll instead", "2.8.0")
  def rollK(r: DRoll): (Dice, Int) = roll(r)

  /** 2k10 means two rolls with dice which results in 1-10*/
  def roll(roll: DRoll): (Dice, Int) = {

    @tailrec
    def it(rollsLeft: Int, acc: Int, dice: Dice): (Dice, Int) = {
      if(rollsLeft > 0) {
        val (newDice, result) = dice.rollInt(roll.pips)
        it(rollsLeft-1, acc + result + 1, newDice)
      } else (dice, acc + roll.modifier)
    }
    it(roll.dices, 0, this)
  }

  def nextK(r: DRoll): Int = roll(r)._2

}

object Dice {

  @deprecated("This is ambiguous. Use either `fromSeed` or `fromState`", "0.2.6")
  def apply(seed: Long): Dice = new Dice(seed * Dice.MULTIPLIER + Dice.INCREMENT)

  /** Turn any value into new random. If you deserialize use `fromState`.
   *
   * @param seed assumes this value is not random enough, will be shuffled.
   */
  def fromSeed(seed: Long): Dice = new Dice(seed * Dice.MULTIPLIER + Dice.INCREMENT)

  /** Create a `Dice` from a number that you know is random enough. Suitable for deserialization.
   *
   * @param state
   * @return
   */
  def fromState(state: Long): Dice = new Dice(state)

  def unsafeRandom: Dice = Dice(Random.nextLong)

  private val DOUBLE_UNIT = 1.0 / (1L << 53)

  private val INCREMENT = 0xda3e39cb94b95bdbL

  private val MULTIPLIER = 0x5851f42d4c957f2dL


  // 2^31 + Int.MaxValue
  // needed because Int/Long are signed and we are the bad guys using them as 32/64 bit sequence
  // and doing .toLong / .asInstanceOf preserves represented values changing underlying bits

  /**
   * has value of 2^31 + Int.MaxValue
   *
   * needed because Int/Long are signed and we are the bad guys using them as 32/64 bit sequence
   * and doing .toLong / .asInstanceOf preserves represented values changing underlying bits
   * ie for negative values it left-fills with ones
   * three/six bits examples:
   * positive, no problem: 010 (2, three bits) => 000010 (2, six bits)
   * negative, problem:  110 (-2, three bits) => 111110(-2, six bits)
   * mask: 000111
   * 111110 & 000111 => 000111
   *
   * todo anything simpler? scodec?
   */
  private val MASK = 4294967295L // 0x00000000ffffffffL

}
