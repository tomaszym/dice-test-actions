# 🎲 Dice – an immutable RNG 🎲

Because we prefer `rollInt` to `nextInt`.


## 🎲 Usage 🎲
sbt:
```scala
"games.datastrophic" %% "dice" % "0.1.2",
```
example:
```scala
val (newDice, randomInt) = dice.rollInt(N)
```

## 🎲 Status 🎲
WIP, but used in our game: [Blackout Age](https://blackoutage.com/) for almost every random thing there.


## 🎲 PCG – Permuted Congruential Generator 🎲
Internal RNG is based on [The PCG Paper](https://www.pcg-random.org), implementation is adapted from [PCG-Java](https://github.com/alexeyr/pcg-java).

Watch [this great lecture](https://www.youtube.com/watch?v=45Oet5qjlms) of the paper's author to learn how that's different from `java.util.Random`.
