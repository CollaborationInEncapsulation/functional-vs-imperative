package edu.fp.examples.app.stages

import edu.fp.examples.app.dto.Message


object MessageMapper {
  val TYPE_KEY = "TYPE"
  val TIMESTAMP_KEY = "TIMESTAMP"
  val PRICE_KEY = "PRICE"
  val QUANTITY_KEY = "QUANTITY"
  val CURRENCY_KEY = "FROMSYMBOL"
  val MARKET_KEY = "MARKET"
  val FLAGS_KEY = "FLAGS"

  def mapToPriceMessage(event: Map[String, Any]): Message[Float] = Message.price(event(PRICE_KEY).asInstanceOf[Float], event(CURRENCY_KEY).asInstanceOf[String], event(MARKET_KEY).asInstanceOf[String])

  def mapToTradeMessage(event: Map[String, Any]): Message[Message.Trade] = Message.trade(event(TIMESTAMP_KEY).asInstanceOf[Float].toLong * 1000, event(PRICE_KEY).asInstanceOf[Float], if (event(FLAGS_KEY).asInstanceOf[String] == "1") event(QUANTITY_KEY).asInstanceOf[Float]
  else -event(QUANTITY_KEY).asInstanceOf[Float], event(CURRENCY_KEY).asInstanceOf[String], event(MARKET_KEY).asInstanceOf[String])

  def isPriceMessageType(event: Map[String, Any]): Boolean = event.contains(TYPE_KEY) && event(TYPE_KEY).asInstanceOf[String] == "5"

  def isValidPriceMessage(event: Map[String, Any]): Boolean = event.contains(PRICE_KEY) && event.contains(CURRENCY_KEY) && event.contains(MARKET_KEY)

  def isTradeMessageType(event: Map[String, Any]): Boolean = event.contains(TYPE_KEY) && event(TYPE_KEY).asInstanceOf[String] == "0"
}

