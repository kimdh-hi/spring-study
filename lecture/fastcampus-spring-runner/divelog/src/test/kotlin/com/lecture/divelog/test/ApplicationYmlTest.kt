package com.lecture.divelog.test

import com.lecture.divelog.common.CustomProperties
import com.lecture.divelog.common.SiteProperties
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("local")
class ApplicationYmlTest(
  private val customProperties: CustomProperties,
  private val siteProperties: SiteProperties
) {

  @Test
  fun randomValuePropertyTest() {
    val randomValue = customProperties.randomValue
    val randomInt = customProperties.randomInt
    val randomLong = customProperties.randomLong
    val randomIntBetween = customProperties.randomIntBetween
    val uuid = customProperties.uuid

    println(randomValue)
    println(randomInt)
    println(randomLong)
    println(randomIntBetween)
    println(uuid)
  }

  @Test
  fun siteProperties() {
    val author = siteProperties.author
    val email = siteProperties.email

    println(author)
    println(email)
  }
}