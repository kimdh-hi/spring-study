package com.toy.springconditional

import com.toy.springconditional.config.AmqpInfo
import com.toy.springconditional.config.AmqpProperties
import com.toy.springconditional.config.CacheConfigInfo
import com.toy.springconditional.config.CacheProperties
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SpringConditionalApplicationTests(
  val cacheProperties: CacheProperties,
	val amqpProperties: AmqpProperties,
  val cacheConfigInfo: CacheConfigInfo,
	val amqpInfo: AmqpInfo
) {

	@Test
	fun contextLoads() {
	}

	@Test
	fun `@ConditionalOnProperty cacheConfigTest`() {

		when (cacheProperties.cacheName) {
			"redis" -> {
				println("REDIS")
				assertEquals("REDIS", cacheConfigInfo.getName())
			}
			"ehcache" -> {
				println("EHCACHE")
				assertEquals("EHCACHE", cacheConfigInfo.getName())
			}
		}
	}

	@Test
	fun `@Conditional amqpConfigTest`() {
		if(amqpProperties.enabled) {
			println("amqpInfo bean registered")
			assertNotNull(amqpInfo)
		}
		else {
			println("amqpInfo bean not registered")
			assertNull(amqpInfo)
		}
	}
}
