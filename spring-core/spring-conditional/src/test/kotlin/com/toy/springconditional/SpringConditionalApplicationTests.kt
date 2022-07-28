package com.toy.springconditional

import com.toy.springconditional.config.CacheConfigInfo
import com.toy.springconditional.config.CustomProperties
import com.toy.springconditional.config.RedisConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SpringConditionalApplicationTests(
	val customProperties: CustomProperties,
	val cacheConfigInfo: CacheConfigInfo
) {

	@Test
	fun contextLoads() {
	}

	@Test
	fun test() {
		customProperties.redis?.let {
			println("REDIS")
			assertEquals("REDIS", cacheConfigInfo.getName())
		}

		customProperties.ehcache?.let {
			println("EHCACHE")
			assertEquals("EHCACHE", cacheConfigInfo.getName())
		}
	}

}
