package edu.fp.examples.java;

import static java.lang.Math.pow;

import java.util.Arrays;

public class example_1_SimpleFunction {

  static long factor = 2;

  public static double[] square(double[] elements) {
    final double[] elementsInPow = new double[elements.length];
    for (int i = 0; i < elements.length; i++) {
      elementsInPow[i] = pow(elements[i], factor);
    }

    return elementsInPow;
  }

  public static void main(String[] args) {
    double[] elements = new double[]{1, 2, 3, 4, 5, 6};

    System.out.println(Arrays.toString(square(elements)));
    factor = 3;
    System.out.println(Arrays.toString(square(elements)));
    // TODO: add one more
  }
}
