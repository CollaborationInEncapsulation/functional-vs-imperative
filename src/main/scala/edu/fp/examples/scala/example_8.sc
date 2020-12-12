val list: List[Int] = List(1, 2, 3)

(4 :: Nil) :++ list

val list2 = 6 :: 5 :: 4 :: 3 :: 2 :: 1 :: 0 :: Nil


list2
  .filter(_ > 0)
  .reduceOption[Int]((total, elementInCollection) => total * elementInCollection)
  .getOrElse(1)

val map = Map(
  "somekey" -> 12,
  "somekey1z" -> 13
)
// same set of ops on Tuple2
//map
//  .map()