package edu.fp.examples.app.stages

import akka.stream.alpakka.mongodb.scaladsl.MongoSink
import akka.stream.scaladsl.{Flow, Sink}
import com.mongodb.reactivestreams.client.{MongoClients, MongoCollection}
import edu.fp.examples.app.dto.Message
import edu.fp.examples.app.dto.Message.Trade
import org.bson.Document

object MongoDBSink {
  val dbName = "crypto"
  val collectionName = "trades"

  val collection: MongoCollection[Document] = MongoClients.create().getDatabase(dbName).getCollection(collectionName)

  def apply(): Sink[Message[Trade], Any] =
    Flow[Message[Trade]]
      .map(m => new Document()
        .append("price", m.data.price)
        .append("amount", m.data.amount)
        .append("currency", m.currency)
        .append("market", m.market)
        .append("timestamp", m.timestamp)
      )
      .to(MongoSink.insertOne(collection))
}
