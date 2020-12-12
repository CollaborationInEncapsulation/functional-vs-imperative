var negate = (b: Int) => if (b < 0) b else -b
var abs = (b: Int) => if (b < 0) -b else b
var increment = (b: Int) => b + 1
var decrement = (b: Int) => b - 1
val pow2: Int => Int = input => input
def pow(degree: Int, value: Int): Int = {
  // FIXME
  null
}

def compose(fn1: Int => Int, fn2: Int => Int): Int => Int =
  x => fn1(fn2(x))



val pipe: Int => Int = negate
  .andThen(abs)
  .andThen(increment)
  .andThen(negate)
  .andThen(pow(2, _))
  (1)

pipe(1)
