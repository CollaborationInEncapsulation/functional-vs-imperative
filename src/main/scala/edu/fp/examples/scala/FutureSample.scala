package edu.fp.examples.scala

import java.util.concurrent.{CountDownLatch, Executors}
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future, Promise}
import scala.util.Success

object FutureSample {
  def main(args: Array[String]): Unit = {

    val latch = new CountDownLatch(3)

    new Thread(() => {
      Thread.sleep(100)
      println("Completed first bit")
      latch.countDown()
    }).start()


    new Thread(() => {
      Thread.sleep(1000)
      println("Completed second bit")
      latch.countDown()
    }).start()


    new Thread(() => {
      Thread.sleep(5000)
      println("Completed third bit")
      latch.countDown()
    }).start()

    latch.await()

    println("Work has been completed")
  }
}
