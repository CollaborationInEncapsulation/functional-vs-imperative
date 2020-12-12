package edu.fp.examples.scala

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future, Promise, blocking}
import scala.util.{Failure, Success}

final case class Value[T](private val value: T) extends (() => T) with SourceOps[T, Value] {
  def apply(): T = value

  override def factory[R](value: => R): Value[R] = new Value[R](value)
}

sealed trait Maybe[T] extends (() => T) with SourceOps[T, Maybe] {
  def hasValue: Boolean
}

case class Something[T](private val value: T) extends Maybe[T]{
  override def hasValue: Boolean = true

  override def apply(): T = value

  override def factory[R](value: => R): Maybe[R] = Something(value)
}

case class Nothing[T]() extends Maybe[T] {
  override def hasValue: Boolean = false

  override def apply(): T = null.asInstanceOf[T]

  override def factory[R](value: => R): Maybe[R] = Nothing()
}

trait SourceOps[T, CONTAINER[X] <: () => X] extends (() => T) {
  //                    lazy
  def factory[R](value: => R): CONTAINER[R];

  def map[R](mapper: T => R): CONTAINER[R] = factory[R](mapper(this()))

  def filter(predicate: T => Boolean): Maybe[T] = this() match {
    case x if x != null && predicate(x) => Something(x)
    case _ => Nothing()
  }

  def flatMap[R](flatMapper: T => CONTAINER[R]): CONTAINER[R] = flatMapper(this())

  def zip[T2](container: CONTAINER[T2]): CONTAINER[Tuple2[T, T2]] = {
    val tuple: Tuple2[T,T2] = (this(), container())
    factory(tuple)
  }
}



object Main {


  def main(args: Array[String]): Unit = {

    implicit val global: ExecutionContextExecutor = ExecutionContext.global

    Future {
      blocking {
        Thread.sleep(1000)
        1
      }
    }
      .map { value =>
        println(value)
        value + ""
      }
//      .onComplete {
//        case Success(value) => println(value)
//        case Failure(exception) => println(exception)
//      }

    val promise = Promise[String]()


    promise
      .future
      .onComplete {
        case Success(x) => println(x)
        case Failure(x) => println(x)
      }

    println("test")

    Thread.sleep(2000)
    promise.success("testValue")
    println("test")


    val value1: Value[String] = Value[String]("-1")
      .map(strValue => strValue.toInt)
      .map(_.toString)
      .map(_.toInt)
      .flatMap(value => Value(value + "Abcd"))

    val value2: Value[Int] = Value[String]("-1")
      .map(strValue => strValue.toInt)
      .map(_.toString)
      .map(_.toInt)

    val value: Value[(Int, String)] = value2.zip(value1)
    println(value
      .map(tuple2 => tuple2._2 + tuple2._1)
      .apply() // terminal
    )
  }
}


//
//trait SourceOps[T, V[X] <: () => X] extends (() => T) {
//  self =>
//  protected def create[R](v: => R): V[R]
//
//  def map[R](mapper: T => R): V[R] = create[R](mapper(self()))
//
//  def filter(predicate: T => Boolean): Maybe[T] = self() match {
//    case x if x != null && predicate(x) => Something(x)
//    case _ => new Nothing()
//  }
//}
//
//sealed trait Maybe[T] extends (() => T) with SourceOps[T, Maybe] {
//  def isPresent: Boolean
//
//  def asValue[B >: T](default: => B): Value[B]
//
//  def apply(): T
//}
//
//case class Something[T](value: T) extends Maybe[T] {
//  def isPresent = true
//
//  def asValue[B >: T](default: => B): Value[B] = new Value[B](this())
//
//  override protected def create[R](v: => R): Something[R] = new Something[R](v)
//
//  override def apply(): T = value
//}
//
//class Nothing[T] extends Maybe[T] {
//  def isPresent = false
//
//  def asValue[B >: T](default: => B) = new Value[B](default)
//
//  def apply() = null.asInstanceOf[T]
//
//  override protected def create[R](v: => R): Maybe[R] = this.asInstanceOf[Maybe[R]]
//}
//
//object Main {
//  def main(args: Array[String]): Unit = {
//    println(Value(1)
//      .filter(_ => false)
//      .asValue(" sda ")())
//  }
//}