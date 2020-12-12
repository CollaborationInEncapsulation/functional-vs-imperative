package edu.fp.examples.java;

import java.util.ArrayList;
import java.util.List;

public class ForLoopSample {

  public static void main(String... args) {
    final List<Integer> values = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      if (i % 2 == 0) {
        values.add(i);
      }
    }

    System.out.println(values);
  }
}
