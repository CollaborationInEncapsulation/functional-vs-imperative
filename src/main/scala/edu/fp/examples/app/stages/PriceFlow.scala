package edu.fp.examples.app.stages

import akka.stream.scaladsl.Flow
import edu.fp.examples.app.dto.Message

object PriceFlow {
  def apply(): Flow[Map[String, Any], Message[Float], Any] =
    Flow[Map[String, Any]]
      .filter(MessageMapper.isPriceMessageType)
      .filter(MessageMapper.isValidPriceMessage)
      .map(MessageMapper.mapToPriceMessage)
}
