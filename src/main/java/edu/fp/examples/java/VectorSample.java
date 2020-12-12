package edu.fp.examples.java;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.StringJoiner;

class Vector3 {

  double x, y, z;

  Vector3(double x, double y, double z) {

    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector3 add(Vector3 v) {
    this.x += v.x;
    this.y += v.y;
    this.z += v.z;

    return this;
  }


  @Override
  public String toString() {
    return new StringJoiner(", ", Vector3.class.getSimpleName() + "[", "]")
        .add("x=" + x)
        .add("y=" + y)
        .add("z=" + z)
        .toString();
  }
}

public class VectorSample {

  public static void main(String... args) {
    final var vector = new Vector3(1, 0, 0);

    System.out.println(vector);

    System.out.println("expected the twice length of the Unit vector is two : " + twiceLength(vector));
    System.out.println("expected the length of the Unit vector is one : " + length(vector));
  }

  private static double twiceLength(Vector3 vector) {
    vector = vector.add(vector);

    return length(vector);
  }

  private static double length(Vector3 vector) {
    return sqrt(pow(vector.x, 2) + pow(vector.y, 2) + pow(vector.z, 2));
  }
}
