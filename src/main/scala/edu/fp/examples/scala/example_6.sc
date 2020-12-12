
// Define a function
// that may define recursive function

// var fib: Int => Long = null
//
// fib = x => fib(x - 1) + fib(x - 2)

// f(x): (Int => Long) => (Int => Long) => {
//   var innerFun: (Int => Long) = x => f(x)
// }

type RecursionFunction[IN, OUT] = IN => OUT // class abstraction

type AbstractRecursionFactory[IN, OUT] = RecursionFunction[IN, OUT] => RecursionFunction[IN, OUT]


def recursionFactoryMethod[IN, OUT](arf: AbstractRecursionFactory[IN, OUT]): RecursionFunction[IN, OUT] = {
  x => arf(recursionFactoryMethod(arf))(x)
}

var fibImpl = recursionFactoryMethod[Int, Double](fibFn => { // concrete implementation abstract factory for fibonacci case
  x =>
    if (x == 0) 0
    else if (x == 1) 1
    else fibFn(x - 1) + fibFn(x - 2)
})

println(fibImpl(0))
println(fibImpl(1))
println(fibImpl(5))
println(fibImpl(6))

