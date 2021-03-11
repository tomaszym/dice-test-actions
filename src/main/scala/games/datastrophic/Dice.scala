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

    (Dice(state * Dice.MULTIPLIER + Dice.INCREMENT), Integer.rotateRight(xorShifted, rot))
  }

  private def roll64Bits: (Dice, Long) = {
    val (d, i) = rollBits(32)
    val (d2, i2) = d.rollBits(32)

    val l = (i << 32) + i2
    (d2, l)
  }

  private def rollBits(bits: Int) = {
    val (next, result) = roll32Bits
    (next, result >>> 32-bits)
  }

  def rollIntMaxValue: (Dice, Int) = roll32Bits

  def rollInt(bound: Int): (Dice, Int) = {
    val (d, i) = roll32Bits
    (d, i % bound)
  }
  def nextInt(bound: Int): Int = rollInt(bound)._2

  def rollDouble: (Dice, Double) = {
    val (dice, i) = roll64Bits
    (dice, (i >>> 11) * Dice.DOUBLE_UNIT)
  }

  def rollNormal(standardDeviation: Double, expectedValue: Double): (Dice, (Double, Double)) = {

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

  /** 2k10 means two rolls with dice which results in 1-10*/
  def rollK(roll: DRoll): (Dice, Int) = {

    @tailrec
    def it(rollsLeft: Int, acc: Int, dice: Dice): (Dice, Int) = {
      if(rollsLeft > 0) {
        val (newDice, result) = dice.rollInt(roll.pips)
        it(rollsLeft-1, acc + result + 1, newDice)
      } else (dice, acc + roll.modifier)
    }
    it(roll.dices, 0, this)
  }

  def nextK(roll: DRoll): Int = rollK(roll)._2

}

object Dice {

  def apply(seed: Long): Dice = new Dice(seed).roll64Bits._1

  def unsafeRandom: Dice = Dice(Random.nextLong)

  private val DOUBLE_UNIT = 1.0 / (1L << 53)

  private val INCREMENT = 123456789

  private val MULTIPLIER = 6364136223846793005L

}
