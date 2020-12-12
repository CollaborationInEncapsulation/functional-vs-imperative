// currying

def pow(degree: Int)(x: Double) = {
  var result: Double = x
  for (_ <- 1 until degree)
    result *= result

  result
}

// TODO Define Pow2 Pow3

val pow2: Double => Double = pow(2)
val pow3: Double => Double = pow(3)

println(pow2(2))
println(pow3(2))


import java.time.LocalDateTime

class Account {}

class Person() {}

class CreditCard {}

def executeAsync(task: => Unit): Unit = {
  task
}

def resolveAccount(login: String, password: String): Account = {
  null // assume resolves account
}

def resolvePerson(account: Account): Person = {
  null // assume resolves preson
}

def resolveCard(person: Person): CreditCard = {
  null // assume resolves credit card
}

def resolvePersonCardTransactions(
                                   startDate: LocalDateTime,
                                   endDate: LocalDateTime
                                 )
                                 (implicit
                                  person: Person,
                                  creditCard: CreditCard
                                 ): String = {
  // to something here

  "logs(...)"
}

executeAsync {
  implicit val account: Account = resolveAccount("", "")
  executeAsync {
    implicit val person: Person = resolvePerson(account)
    executeAsync {
      implicit val card: CreditCard = resolveCard(person)

      val resolveTransactions: (LocalDateTime, LocalDateTime) => String = resolvePersonCardTransactions

      println(resolveTransactions(LocalDateTime.now().minusHours(1), LocalDateTime.now()))
    }
  }
}





















// closure = visibility scope
// currying = lazyability
// for
// class








