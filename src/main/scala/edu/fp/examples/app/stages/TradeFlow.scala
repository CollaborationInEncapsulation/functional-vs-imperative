package edu.fp.examples.app.stages

import akka.stream.scaladsl.Flow
import edu.fp.examples.app.dto.Message.Trade
import edu.fp.examples.app.dto.Message

object TradeFlow {
  def apply(): Flow[Map[String, Any], Message[Trade], Any] =
    Flow[Map[String, Any]]
      .filter(MessageMapper.isTradeMessageType)
      .map(MessageMapper.mapToTradeMessage)
}
