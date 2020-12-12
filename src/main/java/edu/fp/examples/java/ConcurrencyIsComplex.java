package edu.fp.examples.java;

public class ConcurrencyIsComplex {

  static long x = 0L;

  public static void main(String... args) throws InterruptedException {

    final Thread thread1 = new Thread(() -> x = 1L);
    final Thread thread2 = new Thread(() -> x = -1L);

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();

  }
}