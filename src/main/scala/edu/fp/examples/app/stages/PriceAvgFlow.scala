package edu.fp.examples.app.stages

import akka.stream.scaladsl.Flow
import edu.fp.examples.app.dto.Message

import java.time.{Duration, Instant}

object PriceAvgFlow {

  val duration: Duration = Duration.ofSeconds(10)

  def apply(): Flow[Message[Float], Message[Float], Any] = Flow[Message[Float]]
    .map(elem => (elem, Instant.now()))
    .statefulMapConcat(() => {
      // stateful decision in statefulMapConcat
      // keep track of time bucket (one per second)
      var currentTimeBucket = Instant.now()

      {
        case (elem, timestamp) =>
          val currentDuration = Duration.between(currentTimeBucket, timestamp)
          val newBucket = duration.compareTo(currentDuration) == -1
          if (newBucket) {
            val times = currentDuration.dividedBy(duration)
            currentTimeBucket = currentTimeBucket.plus(duration.multipliedBy(times))
          }
          List((elem, newBucket))
      }
    })
    .splitWhen(_._2)
    .map(_._1)
    .groupBy(256, _.currency)
    .map(m => m.currency -> m.data)
    .fold((0f, 0, ""))((agg, m) => agg match {
      case (total, cnt, _) =>
        val (currency, price) = m
        (total + price, cnt + 1, currency)
    })
    .map {
      case (total, cnt, currency) => Message.avg(total / cnt, currency, "CryptoCompare")
    }
    .mergeSubstreams
    .mergeSubstreams


}
