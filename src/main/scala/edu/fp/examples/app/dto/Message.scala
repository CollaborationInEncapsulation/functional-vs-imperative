package edu.fp.examples.app.dto

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import edu.fp.examples.app.dto.Message.MessageType

import java.time.Instant


object Message extends Enumeration {

  class MessageType extends TypeReference[Message.type]

  type Type = Value
  val PRICE, AVG, TRADE = Value

  def avg(avg: Float, currency: String, market: String) = new Message[Float](Instant.now.toEpochMilli, avg, currency, market, AVG)

  def price(price: Float, currency: String, market: String) = new Message[Float](Instant.now.toEpochMilli, price, currency, market, PRICE)

  def trade(timestamp: Long, price: Float, amount: Float, currency: String, market: String) = new Message[Message.Trade](timestamp, Message.Trade(price, amount), currency, market, TRADE)

  case class Trade(price: Float, amount: Float)

}

case class Message[+T](timestamp: Long, data: T, currency: String, market: String, @JsonScalaEnumeration(classOf[MessageType]) `type`: Message.Type)
