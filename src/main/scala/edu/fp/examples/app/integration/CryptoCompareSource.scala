package edu.fp.examples.app.integration

import akka.stream.OverflowStrategy
import akka.stream.scaladsl.Source
import io.socket.client.{IO, Socket}

import java.util
import scala.concurrent.ExecutionContext

object CryptoCompareSource {
  def apply(markets: Seq[String]): Source[Map[String, Any], Any] = {
    Source.fromMaterializer((mat, a) => {
      println(s"Here $mat")
      implicit val context: ExecutionContext.parasitic.type = ExecutionContext.parasitic
      val (queue, source) = Source.queue[Map[String, Any]](256, OverflowStrategy.dropHead).preMaterialize()(mat)
      val socket: Socket = IO.socket("https://streamer.cryptocompare.com")

      queue.watchCompletion()
        .onComplete(_ =>{
          println("here")
          socket.disconnect()
        })

      socket
        .on(Socket.EVENT_CONNECT, (_: Array[AnyRef]) => {
          val subscription = markets.toArray
          val subs = new util.HashMap[String, AnyRef]
          subs.put("subs", subscription)
          socket.emit("SubAdd", subs) // { "subs" : ["5~CCCAGG~BTC~USD", "0~Coinbase~BTC~USD", "0~Cexio~BTC~USD"] }
        })
        .on("m", (args: Array[AnyRef]) => {
          args(0).toString match {
            case PriceDecoder(parsedPriceMessage) => queue.offer(parsedPriceMessage)
            case TradeDecoder(parsedTradeMessage) => queue.offer(parsedTradeMessage)
            case _ =>
          }
        })
        .on(Socket.EVENT_ERROR, (args: Array[AnyRef]) => queue.fail(args(0).asInstanceOf[Throwable]))
        .on(Socket.EVENT_DISCONNECT, (_: Array[AnyRef]) => queue.complete())

      socket.connect

      source
    }).async
  }
}
