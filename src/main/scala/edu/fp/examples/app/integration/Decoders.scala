package edu.fp.examples.app.integration

case object PriceDecoder {
  val fields: Array[(String, Int)] = Array(
    "TYPE" -> 0x0, // hex for binary 0, it is a special case of fields that are always there
    "MARKET" -> 0x0,
    "FROMSYMBOL" -> 0x0,
    "TOSYMBOL" -> 0x0,
    "FLAGS" -> 0x0,
    "PRICE" -> 0x1, // hex for binary 1
    "BID" -> 0x2, // hex for binary 10
    "OFFER" -> 0x4, // hex for binary 100
    "LASTUPDATE" -> 0x8, // hex for binary 1000
    "AVG" -> 0x10, // hex for binary 10000
    "LASTVOLUME" -> 0x20, // hex for binary 100000
    "LASTVOLUMETO" -> 0x40, // hex for binary 1000000
    "LASTTRADEID" -> 0x80, // hex for binary 10000000
    "VOLUMEHOUR" -> 0x100, // hex for binary 100000000
    "VOLUMEHOURTO" -> 0x200, // hex for binary 1000000000
    "VOLUME24HOUR" -> 0x400, // hex for binary 10000000000
    "VOLUME24HOURTO" -> 0x800, // hex for binary 100000000000
    "OPENHOUR" -> 0x1000, // hex for binary 1000000000000
    "HIGHHOUR" -> 0x2000, // hex for binary 10000000000000
    "LOWHOUR" -> 0x4000, // hex for binary 100000000000000
    "OPEN24HOUR" -> 0x8000, // hex for binary 1000000000000000
    "HIGH24HOUR" -> 0x10000, // hex for binary 10000000000000000
    "LOW24HOUR" -> 0x20000, // hex for binary 100000000000000000
    "LASTMARKET" -> 0x40000 // hex for binary 1000000000000000000, this is a special case and will only appear on CCCAGG messages)
  )


  def apply(message: String): Map[String, Any] = {

    val valuesArray = message.split("~")
    val valuesArrayLength = valuesArray.length
    val mask = valuesArray(valuesArrayLength - 1)
    val maskInt = Integer.parseInt(mask, 16)

    fields
      .foldLeft((Map[String, Any](), 0))((store, pair) => pair match {
        case (k, v) =>
          v match {
            case 0 =>
              val (s, index) = store
              (s + (k -> valuesArray(index)), index + 1)
            case x if (maskInt & x) > 0 =>
              val (s, index) = store
              //i know this is a hack, for cccagg, future code please don't hate me:(, i did this to avoid
              //subscribing to trades as well in order to show the last market
              if (k.equals("LASTMARKET") || k.equals("LASTTRADEID")) {
                (s + (k -> valuesArray(index)), index + 1)
              } else {
                (s + (k -> valuesArray(index).toFloat), index + 1)
              }
            case _ => store
          }
      })
      ._1
  }

  def unapply(message: String): Option[Map[String, Any]] = {
    if (message.substring(0, message.indexOf("~")) == "5")
      Some(PriceDecoder(message))
    else
      None
  }
}

case object TradeDecoder {

  val fields: Array[(String, Int)] = Array(
    "TYPE" -> 0x0,  // hex for binary 0, it is a special case of fields that are always there          TYPE
    "MARKET" -> 0x0,  // hex for binary 0, it is a special case of fields that are always there        MARKET
    "FROMSYMBOL" -> 0x0, // hex for binary 0, it is a special case of fields that are always there     FROM SYMBOL
    "TOSYMBOL" -> 0x0, // hex for binary 0, it is a special case of fields that are always there       TO SYMBOL
    "FLAGS" -> 0x0, // hex for binary 0, it is a special case of fields that are always there          FLAGS
    "ID" -> 0x1, // hex for binary 1                                                                   ID
    "TIMESTAMP" -> 0x2, // hex for binary 10                                                           TIMESTAMP
    "QUANTITY" -> 0x4, // hex for binary 100                                                           QUANTITY
    "PRICE" -> 0x8,// hex for binary 1000                                                              PRICE
    "TOTAL" -> 0x10 // hex for binary 10000                                                            TOTAL
  )


  def apply(message: String): Map[String, Any] = {
    val valuesArray = message.split("~")
    val valuesArrayLength = valuesArray.length
    val mask = valuesArray(valuesArrayLength - 1)
    val maskInt = Integer.parseInt(mask, 16)

    fields
      .foldLeft((Map[String, Any](), 0))((store, pair) => pair match {
        case (k, v) =>
          v match {
            case 0 =>
              val (s, index) = store
              (s + (k -> valuesArray(index)), index + 1)
            case x if (maskInt & x) > 0 =>
              val (s, index) = store
              (s + (k -> valuesArray(index).toFloat), index + 1)
            case _ => store
          }
      })
      ._1
  }

  def unapply(message: String): Option[Map[String, Any]] = {
    if (message.substring(0, message.indexOf("~")) == "0")
      Some(TradeDecoder(message))
    else
      None
  }
}
