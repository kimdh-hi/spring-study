package com.toy.springwebfluxredis

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@SpringBootTest
class SpringWebfluxRedisApplicationTests {

	@Autowired
	lateinit var template: ReactiveStringRedisTemplate

	@Test
	fun contextLoads() {
		val operation = template.opsForValue()
		val mono = Flux.range(1, 100)
			.flatMap { operation.increment("user:1:visit") }
			.then()
		StepVerifier.create(mono)
			.verifyComplete()
	}

}
