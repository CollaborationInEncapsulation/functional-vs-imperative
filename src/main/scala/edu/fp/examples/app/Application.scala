package edu.fp.examples.app

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives.{concat, get, getFromResource, handleWebSocketMessages, path}
import akka.http.scaladsl.server.PathMatcher
import akka.stream.FlowShape
import akka.stream.scaladsl.GraphDSL.Implicits.fanOut2flow
import akka.stream.scaladsl.{Broadcast, BroadcastHub, Flow, GraphDSL, Keep, Merge, Sink, Source}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import edu.fp.examples.app.dto.Message
import edu.fp.examples.app.integration.CryptoCompareSource
import edu.fp.examples.app.stages.{MongoDBSink, PriceAvgFlow, PriceFlow, TradeFlow}

import scala.concurrent.{ExecutionContextExecutor, Future}


object Application extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val mapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)

  private val graphSource = CryptoCompareSource(Seq("5~CCCAGG~BTC~USD", "0~Coinbase~BTC~USD", "0~Cexio~BTC~USD"))
    .via(GraphDSL.create() { implicit graphBuilder =>
      val IN = graphBuilder.add(Broadcast[Map[String, Any]](2))
      val PRICE = graphBuilder.add(Broadcast[Message[Float]](2))
      val TRADE = graphBuilder.add(Broadcast[Message[Message.Trade]](1))
      val OUT = graphBuilder.add(Merge[Message[Any]](3))

      IN ~> PriceFlow() ~> PRICE ~> PriceAvgFlow() ~> OUT
                           PRICE                   ~> OUT
      IN ~> TradeFlow() ~> TRADE                   ~> OUT
                           TRADE ~> MongoDBSink()

      FlowShape(IN.in, OUT.out)
    })
    .toMat(BroadcastHub.sink)(Keep.right)
    .run

  val serverSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
    Http().newServerAt("localhost", 8080).connectionSource()


  val bindingFuture =
    serverSource.runForeach { connection => // foreach materializes the source
      println("Accepted new connection from " + connection.remoteAddress)
      // ... and then actually handle the connection
      connection.handleWith(
        get {
          concat(
            path("") {
              getFromResource("./ui/index.html")
            },
            path("main.js") {
              getFromResource("./ui/main.js")
            },
            path("stream") {
              handleWebSocketMessages(Flow.fromSinkAndSource(Sink.ignore, graphSource.map(mapper.writeValueAsString(_)).map(TextMessage(_))))
            }
          )
        }
      )
    }

}
