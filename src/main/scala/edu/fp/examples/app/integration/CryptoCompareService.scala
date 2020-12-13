package edu.fp.examples.app.integration

import akka.actor.typed.ActorSystem
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import akka.stream.{Materializer, SinkRef}
import com.typesafe.config.ConfigFactory
import edu.fp.examples.app.serialization.CborSerializable

object CryptoCompareService {

  val CryptoCompareServiceKey: ServiceKey[CryptoCompareSourceRequest] = ServiceKey[CryptoCompareSourceRequest]("CryptoCompareService")

  final case class CryptoCompareSourceRequest(subscriptions: Seq[String], sink: SinkRef[Map[String, Any]]) extends CborSerializable

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory
      .parseString(
        s"""
        akka.remote.artery.canonical.port = 9091
        akka.cluster.roles = [CryptoCompareService]
        """)
      .withFallback(ConfigFactory.load("application"))
    ActorSystem[CryptoCompareSourceRequest](Behaviors.setup[CryptoCompareSourceRequest](ctx => {
      implicit val materializer: Materializer = Materializer.createMaterializer(ctx)

      ctx.system.receptionist ! Receptionist.Register(CryptoCompareServiceKey, ctx.self)

      Behaviors.receiveMessage[CryptoCompareSourceRequest](sr => {
        println("Received Message")
        CryptoCompareSource(sr.subscriptions)
          .to(sr.sink)
          .run

        Behaviors.same
      })
    }), "StreamingService", config)
  }
}
