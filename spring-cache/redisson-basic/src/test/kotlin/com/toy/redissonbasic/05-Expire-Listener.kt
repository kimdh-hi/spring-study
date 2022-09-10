package com.toy.redissonbasic

import com.toy.redissonbasic.base.BaseTest
import org.junit.jupiter.api.Test
import org.redisson.api.ExpiredObjectListener
import org.redisson.client.codec.StringCodec
import reactor.test.StepVerifier
import java.util.concurrent.TimeUnit

class `05-Expire-Listener`: BaseTest() {

  @Test
  fun `ExpiredListener`() {
    val bucket = client!!.getBucket<String>("user:1:username", StringCodec.INSTANCE)
    val setMono = bucket.set("test", 3, TimeUnit.SECONDS)
    val getMono = bucket.get()
      .doOnNext { println(it) }
      .then()

    // config set notify-keyspace-events AKE (redis-cli)
    val listenerMono = bucket.addListener(object: ExpiredObjectListener {
      override fun onExpired(name: String) {
        println("expired name: $name")
      }
    }).then()

    StepVerifier.create(setMono.concatWith(getMono).concatWith(listenerMono))
      .verifyComplete()

    Thread.sleep(5000)
  }
}