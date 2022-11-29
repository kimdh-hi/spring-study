package com.toy.springgraphql.datasource.fake

import com.github.javafaker.Faker
import com.toy.springgraphqldemo.generated.types.Address
import com.toy.springgraphqldemo.generated.types.Author
import com.toy.springgraphqldemo.generated.types.Book
import com.toy.springgraphqldemo.generated.types.ReleaseHistory
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ThreadLocalRandom
import javax.annotation.PostConstruct

@Configuration
class FakeBookDataSource(
  private val faker: Faker
) {

  companion object {
    val BOOK_LIST = mutableListOf<Book>()
  }

  @PostConstruct
  fun postConstruct() {
    for (i in 0 until 20) {
      val addresses = mutableListOf<Address>()

      val author = Author(
        addresses = addresses,
        name = faker.book().author(),
        originCountry = faker.country().name(),
      )
      val released = ReleaseHistory(
        printedEdition = faker.bool().bool(),
        releasedCountry = faker.country().name(),
        year = faker.number().numberBetween(2020, 2022)
      )
      val book = Book(
        author = author,
        publisher = faker.book().publisher(),
        title = faker.book().title(),
        released = released
      )


      for(j in 0 until ThreadLocalRandom.current().nextInt(1, 3)) {
        val address = Address(
          country = faker.address().country(),
          city = faker.address().cityName(),
          street = faker.address().streetAddress()
        )
        addresses.add(address)
      }

      BOOK_LIST.add(book)
    }
  }
}