//package edu.fp.examples.java;
//
//import java.util.function.Function;
//import scala.Int;
//
//// 1. Transform to Functional and make proper abstraction over functions
//// 2. Add support for Unary Function to express functional composition
//// 3. Add support for lazy evaluation
//// 4. Add support for carrying
//public class Calculator {
//
//  public static void main(String[] args) {
//    int result = calc(1, 2, "+");
//    result = calc(result, 3, "-");
//  }
//
//  static int calc(int x1, int x2, String operator) {
////    return chooseOperator(operator);
//  }
//
//  private static Function<Int, Int> chooseOperator(String operator) {
//    switch (operator) {
//      case "+":
//        return x1 + x2;
//      case "-":
//        return x1 - x2;
//
//      default: throw new IllegalArgumentException("Unsupported operator " + operator);
//    }
//  }
//}