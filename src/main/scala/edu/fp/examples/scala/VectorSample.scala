package edu.fp.examples.scala

trait Vector[T <: Vector[T]] {

  def apply(position: Int): Float

  def add(v: T): T

  def +(v: T): T = add(v)

  def *(v: T): T

  def size(): Int
}

case class Vector2(x: Float, y: Float) extends Vector[Vector2] {

  override def apply(position: Int): Float = position match {
    case 0 => x
    case 1 => y
    case _ => throw new IndexOutOfBoundsException
  }

  override def add(v: Vector2): Vector2 = {
    Vector2(this.x + v.x, this.y + v.y)
  }

  override def *(v: Vector2): Vector2 = new Vector2(this.x * v.x, this.y * v.y)

  override def size(): Int = 2
}

//class Vector3(val x: Float, val y: Float, val z: Float) extends Vector[Vector3] {
//  override def apply(position: Int): Float = position match {
//    case 0 => x
//    case 1 => y
//    case 2 => z
//    case _ => throw new IndexOutOfBoundsException
//  }
//
//  override def add(v: Vector3): Vector3 = {
//    new Vector3(this.x + v.x, this.y + v.y, this.z + v.z)
//  }
//
//  override def size(): Int = 3
//}

object Vector { // singleton

  def sum[T <: Vector[T]](v1: T, v2: T): T = {
    val vector: T = v1 + v2 * v1
    vector
  }

  def main(args: Array[String]): Unit = {
    println(sum(Vector2(2, 2), Vector2(2, 1)))
  }

}

object VectorSample {

  def main(args: Array[String]): Unit = {
//    val vector = new Vector3(1, 0, 0)

//    println(vector)

    //    println(Vector.twiceLength(vector))
    //    println(Vector.twiceLength(vector))
    //    println(Vector.twiceLength(vector))
    //    println(Vector.twiceLength(vector))
    //    println(Vector.twiceLength(vector))
    //    println(Vector.length(vector))
    //    println(Vector.length(vector))
    //    println(Vector.length(vector))
    //    println(Vector.length(vector))
  }
}
