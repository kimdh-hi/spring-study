package com.toy.springwebfluxredis

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.redisson.api.RedissonReactiveClient
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

	@Autowired
	lateinit var redissonClient: RedissonReactiveClient

	// data redis vs redisson 속도비교
	//org.springframework.boot:spring-boot-starter-data-redis-reactive
	//org.redisson:redisson-spring-boot-starter
	@RepeatedTest(3)
	fun springDataRedisTest() {
		val operation = template.opsForValue()

		val start = System.currentTimeMillis()
		val mono = Flux.range(1, 100)
			.flatMap { operation.increment("user:1:visit") }
			.then()
		StepVerifier.create(mono)
			.verifyComplete()
		val end = System.currentTimeMillis()

		println("spend-time: ${end-start}ms")
	}

	@RepeatedTest(3)
	fun redissonClientTest() {
		val atomicLong = redissonClient.getAtomicLong("user:1:visit")

		val start = System.currentTimeMillis()
		val mono = Flux.range(1, 100)
			.flatMap { atomicLong.incrementAndGet() }
			.then()
		StepVerifier.create(mono)
			.verifyComplete()
		val end = System.currentTimeMillis()

		println("spend-time: ${end-start}ms")
	}

}
