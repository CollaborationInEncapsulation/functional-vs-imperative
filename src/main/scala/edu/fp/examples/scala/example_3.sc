  // 1. Name
  // 2. Return Type
  // 3. Body
  // 4. Params
  def calc1(x: Int, y: Int, binaryTransformation: (Int, Int) => Int): () => Int = {
    () => binaryTransformation(x, y)
  }

  def calc2(x: Int, transformation: Int => Int): Int = {
    transformation(x)
  }

  def parserToBinary(operator: String): (Int, Int) => Int = operator match {
      case "+" => (x: Int, y: Int) => x + y
      case "-" => (x: Int, y: Int) => x - y
      case _ => throw new IllegalArgumentException("asdas")
  }

  def parserToUnary(operator: String): Int => Int = operator match {
    case "+" => (x: Int) => if (x < 0) -x else x
    case "-" => (x: Int) => if (x > 0) -x else x
    case _ => throw new IllegalArgumentException("asdas")
  }

  val function: () => Int = calc1(1, 2, parserToBinary("-"))