package `00-base`

import com.github.javafaker.Faker

fun main() {
  for(i in 1..10) {
    println(Faker.instance().name().fullName())
  }
}