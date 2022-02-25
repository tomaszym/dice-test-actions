# 🎲 Dice – an immutable RNG 🎲

Because `rollInt` > `nextInt`.


## 🎲 Usage 🎲
sbt:
```scala
"games.datastrophic" %% "dice" % "0.2.5",
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

### Randomness quality warning
This implementation fails PractRand at 8/16 MB. Do not use for cryptographic purposes. 

```
> sbt run | ./RNG_test stdin -tlmin 4MB
RNG_test using PractRand version 0.94
RNG = RNG_stdin, seed = unknown
test set = core, folding = standard(unknown format)

rng=RNG_stdin, seed=unknown
length= 4 megabytes (2^22 bytes), time= 0.3 seconds
  no anomalies in 124 test result(s)

rng=RNG_stdin, seed=unknown
length= 8 megabytes (2^23 bytes), time= 0.7 seconds
  Test Name                         Raw       Processed     Evaluation
  FPF-14+6/16:cross                 R= +11.0  p =  2.6e-9    VERY SUSPICIOUS 
  ...and 134 test result(s) without anomalies

rng=RNG_stdin, seed=unknown
length= 16 megabytes (2^24 bytes), time= 1.3 seconds
  Test Name                         Raw       Processed     Evaluation
  DC6-9x1Bytes-1                    R=  +5.8  p =  4.3e-3   unusual          
  FPF-14+6/16:all                   R=  +6.1  p =  3.1e-5   mildly suspicious
  FPF-14+6/16:cross                 R= +26.2  p =  4.8e-21    FAIL !!        
  ...and 148 test result(s) without anomalies

```

## 🎲 PCG – Permuted Congruential Generator 🎲
Internal RNG is based on [The PCG Paper](https://www.pcg-random.org), implementation is adapted from [PCG-Java](https://github.com/alexeyr/pcg-java).

Watch [this great lecture](https://www.youtube.com/watch?v=45Oet5qjlms) of the paper's author to learn how that's different from `java.util.Random`.
