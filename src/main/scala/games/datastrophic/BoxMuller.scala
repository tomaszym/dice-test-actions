/*
 * Copyright 2020 Tomasz Szymula
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package games.datastrophic

/** Box-Muller transform – sample two normally
 * distributed numbers given two uniformly distributed ones.
 *
 * adapted https://github.com/mmiklavc/box-muller
 *
 *  */
object BoxMuller {
  private val twicePI: Double = 2 * Math.PI

  def apply(
    a: Double,
    b: Double,
  ): (Double, Double) = {
    val x = Math.sqrt(-2.0 * Math.log(a)) * Math.cos(twicePI * b)
    val y = Math.sqrt(-2.0 * Math.log(a)) * Math.sin(twicePI * b)

    (x,y)
  }

}
