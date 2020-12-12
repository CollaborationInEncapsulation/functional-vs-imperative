package edu.fp.examples.scala

import java.util.concurrent.{Executors, TimeUnit}
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.{ExecutionContext, Future, blocking}
import scala.util.{Failure, Success}

object WorkWithThreads {

  def main(args: Array[String]): Unit = {
    implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(4))
    val future: Future[String] = Future[String] {
      blocking {
        Thread.sleep(100)
        "1"
      }
    }

    future
      .map(value => {
        println(s"Execution on ${Thread.currentThread().getName}")
        value.toInt
      })
      .map(value => {
        println(s"Execution on ${Thread.currentThread().getName}")
        throw new RuntimeException("test")
        value + "asdasdasd"
      })
      .onComplete {
        case Success(value) => println(s"Result of exec $value")
        case Failure(throwable) => println(s"exec faild with $throwable")
      }





















//    val contextExecutorService = ExecutionContext.fromExecutorService(
//      Executors.newFixedThreadPool(8000)
//    )
//    val counter = new AtomicInteger(0)
//
//    val start = System.nanoTime()
//    for (i <- 0 until 10000) {
//      contextExecutorService.execute(() => counter.addAndGet(i))
//    }
//    contextExecutorService.shutdown()
//    contextExecutorService.awaitTermination(100, TimeUnit.SECONDS)
//    val end = System.nanoTime()
//
//    println(s"Result: ${counter.get()}");
//    println(s"Execution took ${(end - start) / 1000000}ms")





















//    val range: Seq[Int] = for (i <- 0 to 10000) yield i
//
//    val tuple = range.splitAt(5000)
//
//    val start = System.nanoTime()
//        val thread: Thread = new Thread(() => {
//          val sum =
//            tuple._1
//              .reduce[Int]((total, nextValue) => total + nextValue)
//
//          println(s"result = $sum")
//        })
//
//        val thread2 = new Thread(() => {
//          val sum =
//            tuple._2
//              .reduce[Int]((total, nextValue) => total + nextValue)
//
//          println(s"result = $sum")
//        })
//
//        thread2.start()
//        thread.start()
//
//        thread.join()
//        thread2.join()





//    val sum =
//    range.reduce[Int]((total, nextValue) => total + nextValue)
//    println(s"result = $sum")
//    val end = System.nanoTime()

  }
}
