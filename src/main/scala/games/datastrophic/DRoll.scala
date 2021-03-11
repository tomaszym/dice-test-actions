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
)

object DRoll {
  def k100: DRoll = DRoll(1,100,0)
}
