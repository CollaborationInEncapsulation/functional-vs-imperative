package edu.fp.examples.scala

import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, RunnableGraph, Sink, Source, SubFlow}
import akka.util.ByteString

import java.nio.file.Paths
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object AkkaStreamsSample {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    val source: Source[ByteString, Future[IOResult]] = FileIO.fromPath(Paths.get("/Users/olehdokuka/Documents/Workspace/Scala/functional-vs-imperative/src/main/resources/words.txt"), 512)

    val subFlow = source
      .mapConcat(bs => bs)
      .map(_.toChar)
      .delay(4.seconds)
      .splitAfter(character => character == '\n')
      .fold("")(_ + _)

    val sink = Sink.foreach(println)


    subFlow.to(sink)
      .run

  }

}
