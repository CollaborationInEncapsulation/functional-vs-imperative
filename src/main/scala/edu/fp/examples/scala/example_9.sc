import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{Executors, ForkJoinPool, RecursiveTask, TimeUnit}
import scala.collection.parallel.CollectionConverters.seqIsParallelizable
import scala.collection.parallel.ParSeq
import scala.concurrent.ExecutionContext

//val range: Seq[Int] = for (i <- 0 to 10000) yield i
//
//
//
//val parrallelSeq: ParSeq[Int] = range.par
//val transformed = parrallelSeq
//  .map(a => {
//    a + "1"
//  })
//
//transformed
//  .filter(f => f.length > 2)


val contextExecutorService = ExecutionContext.fromExecutorService(
  Executors.newFixedThreadPool(2)
)
val counter = new AtomicInteger(0)

val start = System.nanoTime()
for (i <- 0 until 10000) {
  contextExecutorService.execute(() => counter.addAndGet(i))
}
contextExecutorService.shutdown()
contextExecutorService.awaitTermination(100, TimeUnit.SECONDS)
val end = System.nanoTime()
println(s"Result: ${counter.get()}");


//transformed
//  .map()
//  .map()
//  .map()
//  .map()
//  .map()





//val pool = new ForkJoinPool(8)
//
//case class ProcessingTask(sequence: Seq[Int], start: Int, end: Int) extends RecursiveTask[Int]() {
//
//  override def compute(): Int = {
//    println(s"Doing computation on ${Thread.currentThread().getName}")
//    val length = end - start
//
//    if (length < 100) {
//      sequence
//        .slice(start, end)
//        .sum
//    } else {
//      val offset = length / 2
//      val task1 = ProcessingTask(sequence, start + offset, end)
//      val task2 = ProcessingTask(sequence, start, start + offset)
//
//      task1.fork()
//      val partialSum = task2.compute()
//
//      partialSum + task1.join()
//    }
//  }
//}
//
//val resultTask = pool.submit(new ProcessingTask(range, 0, range.length))
//
//println(resultTask.join())