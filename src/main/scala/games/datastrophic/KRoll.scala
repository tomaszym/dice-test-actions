/*
 * Copyright 2020 Tomasz Szymula
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package games.datastrophic

case class KRoll(
  dices: Int,
  pips: Int,
  modifier: Int
)

object KRoll {
  def k100: KRoll = KRoll(1,100,0)
}
