# 🎲 Dice – an immutable RNG 🎲

Because `rollInt` > `nextInt`.


## 🎲 Usage 🎲
sbt:
```scala
"games.datastrophic" %% "dice" % "0.2.0",
```
An example:
```scala
val (newDice, randomInt) = dice.rollInt(N)
```

This works well with `State`:
```scala
for {

  amount <- State[Dice, Int](_.rollInt(math.max(1, math.pow(value, 1d / 9).toInt)))
  
  spread <- State[Dice, ValueSpread](_.unsafeRollOneOf(List(SpreadEqually, SpreadStepOne, SpreadSquare)))
  
  bar <- foo(amount, spread)
  
} yield bar
```


## 🎲 Status 🎲
WIP, but used in our game: [Blackout Age](https://blackoutage.com/) for almost every random thing there.


## 🎲 PCG – Permuted Congruential Generator 🎲
Internal RNG is based on [The PCG Paper](https://www.pcg-random.org), implementation is adapted from [PCG-Java](https://github.com/alexeyr/pcg-java).

Watch [this great lecture](https://www.youtube.com/watch?v=45Oet5qjlms) of the paper's author to learn how that's different from `java.util.Random`.
