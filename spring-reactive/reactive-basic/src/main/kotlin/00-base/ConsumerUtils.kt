package `00-base`

import com.github.javafaker.Faker
import java.util.function.Consumer

object ConsumerUtils {
  fun onNext(): Consumer<Any> = Consumer { o -> println("Received...: $o") }

  fun onError(): Consumer<Throwable> = Consumer { e -> println("Error...: ${e.message}") }

  fun onComplete(): Runnable = Runnable { println("Complete...") }
}

object FakerUtils {
  val FAKER = Faker.instance()

  fun getFullName() = FAKER.name().fullName()
  fun getFirstName() = FAKER.name().firstName()
}