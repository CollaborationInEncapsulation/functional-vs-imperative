package edu.fp.examples.app

import akka.actor.typed.ActorSystem
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives.{concat, get, getFromResource, handleWebSocketMessages, path}
import akka.stream.scaladsl.GraphDSL.Implicits.fanOut2flow
import akka.stream.scaladsl.{Broadcast, BroadcastHub, Flow, GraphDSL, Keep, Merge, Sink, Source, StreamRefs}
import akka.stream.{FlowShape, Materializer}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.config.ConfigFactory
import edu.fp.examples.app.dto.Message
import edu.fp.examples.app.integration.CryptoCompareService
import edu.fp.examples.app.integration.CryptoCompareService.CryptoCompareSourceRequest
import edu.fp.examples.app.stages.{MongoDBSink, PriceAvgFlow, PriceFlow, TradeFlow}

import scala.concurrent.{ExecutionContextExecutor, Future}


object GatewayService extends App {
  val config = ConfigFactory
    .parseString(
      s"""
        akka.remote.artery.canonical.port = 9090
        akka.cluster.roles = [GatewayService]
        """)
    .withFallback(ConfigFactory.load("application"))

  ActorSystem(Behaviors.setup[Any](implicit ctx => {
    implicit val materializer: Materializer = Materializer.createMaterializer(ctx)
    implicit val executionContext: ExecutionContextExecutor = ctx.executionContext

    val mapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)

    val (sinkRef, graphSource) = StreamRefs.sinkRef[Map[String, Any]]
      .via(GraphDSL.create() { implicit graphBuilder =>
        val IN = graphBuilder.add(Broadcast[Map[String, Any]](2))
        val PRICE = graphBuilder.add(Broadcast[Message[Float]](2))
        val TRADE = graphBuilder.add(Broadcast[Message[Message.Trade]](2))
        val OUT = graphBuilder.add(Merge[Message[Any]](3))

        IN ~> PriceFlow() ~> PRICE ~> PriceAvgFlow() ~> OUT
                             PRICE                   ~> OUT
        IN ~> TradeFlow() ~> TRADE                   ~> OUT
                             TRADE ~> MongoDBSink()

        FlowShape(IN.in, OUT.out)
      })
      .toMat(BroadcastHub.sink)(Keep.both)
      .run


    val subscriptionAdapter = ctx.messageAdapter[Receptionist.Listing] {
      case CryptoCompareService.CryptoCompareServiceKey.Listing(services) =>
        if (services.nonEmpty)
          services.head ! CryptoCompareSourceRequest(Seq("5~CCCAGG~BTC~USD", "0~Coinbase~BTC~USD", "0~Cexio~BTC~USD"), sinkRef)
    }

    ctx.system.receptionist ! Receptionist.Subscribe(CryptoCompareService.CryptoCompareServiceKey, subscriptionAdapter)

    val serverSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
      Http()(ctx.system).newServerAt("localhost", 8080).connectionSource()

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

    Behaviors.empty
  }), "StreamingService", config)
}
