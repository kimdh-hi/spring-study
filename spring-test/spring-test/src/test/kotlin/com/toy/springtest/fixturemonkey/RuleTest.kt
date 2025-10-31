package com.toy.springtest.fixturemonkey

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.api.plugin.SimpleValueJqwikPlugin
import com.navercorp.fixturemonkey.datafaker.plugin.DataFakerPlugin
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import net.jqwik.api.Arbitraries
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.slf4j.LoggerFactory

class RuleTest {

  private val log = LoggerFactory.getLogger(RuleTest::class.java)

  private val fixtureMonkey: FixtureMonkey = FixtureMonkey.builder()
    .plugin(KotlinPlugin())
    .plugin(SimpleValueJqwikPlugin())
    .plugin(DataFakerPlugin())
    .build()

  private val userFixtureBuilder = fixtureMonkey.giveMeKotlinBuilder<User>()
    .set(User::id, Arbitraries.strings().ofMinLength(11).ofMaxLength(50).alpha())

  private data class User(val id: String, val name: String, val email: String) {
    init {
      check(id.length in 11..50) { "too long or short id. length=${id.length}" }
    }
  }

  @Test
  fun test() {
    repeat(100) {
      val user = assertDoesNotThrow { userFixtureBuilder.sample() }
      log.info("user={}", user)
    }
  }
}
