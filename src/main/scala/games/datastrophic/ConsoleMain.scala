//package games.datastrophic
//
//import scala.annotation.tailrec
//
//object ConsoleMain extends App {
//
//  def chunk(d: Dice, length: Int = 1024): (Dice, Array[Byte]) = {
//    val acc = LazyList.iterate(d.rollIntMaxValue)(_._1.rollIntMaxValue).take(length)
//
//    acc.last._1 -> acc.toArray.flatMap(pair => BigInt(pair._2).toByteArray)
//  }
//
//  @tailrec
//  def loop(d: Dice): Unit = {
//    val (newDice, bytes) = chunk(d)
//    System.out.write(bytes)
//    loop(newDice)
//  }
//
//  loop(Dice(0x853c49e6748fea9bL))
//
//  System.out.flush()
//  System.out.close()
//}
