/*
 * Copyright 2020 Tomasz Szymula
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package games.datastrophic

case class DRoll(
  dices: Int,
  pips: Int,
  modifier: Int
) {
  def max: Int = dices * pips + modifier

  def isAlwaysZero: Boolean = this match {
    case DRoll(0, _, 0) => true
    case DRoll(_, 0, 0) => true
    case _: DRoll => false
  }
}

object DRoll {
  def k100: DRoll = DRoll(1,100,0)
}
