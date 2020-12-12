package edu.fp.examples.java;

public class example_2_Fibonacci {


  public static void main(String[] args) {
    System.out.println(fib(0));
    System.out.println(fib(1));
    System.out.println(fib(2));
    System.out.println(fib(3));
    System.out.println(fib(4));
    long particalValue;
    if (true) {
      particalValue = 1;
    } else {
      particalValue = 0;
    }
  }

static long fib(int n) {
  if (n == 0) {
    return 0;
  }

  long current = 1;
  long previous = 0;

  for (int i = 1; i < n; i++) {
    long oldCurrent = current;

    current += previous;
    previous = oldCurrent;
  }

  return current;
}
}
