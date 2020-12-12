//def fib(n: Int): Long =
//  if (n == 0) 0
//  else if (n == 1) 1
//  else fib(n - 1) + fib(n - 2)

import scala.annotation.tailrec

// 0 + 1; 1 + 1
@tailrec def fibHelper(currentState: Long, previousState: Long, n: Int): Long = {
  if (n == 0) return currentState


  fibHelper(currentState + previousState, currentState, n - 1)
}


def fib(n: Int): Long = if (n == 0) 0 else fibHelper(1, 0, n - 1)

println(fib(0))