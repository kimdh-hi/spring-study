package com.toy.redissonbasic

import com.toy.redissonbasic.base.BaseTest
import org.junit.jupiter.api.Test
import org.redisson.api.DeletedObjectListener
import org.redisson.api.ExpiredObjectListener
import org.redisson.client.codec.StringCodec
import reactor.test.StepVerifier
import java.util.concurrent.TimeUnit

class `05-EventListener`: BaseTest() {

  @Test
  fun `expireEventListener`() {
    val bucket = client!!.getBucket<String>("user:1:username", StringCodec.INSTANCE)
    val setMono = bucket.set("test", 3, TimeUnit.SECONDS)
    val getMono = bucket.get()
      .doOnNext { println(it) }
      .then()

    // config set notify-keyspace-events AKE (redis-cli)
    val listenerMono = bucket.addListener(object: ExpiredObjectListener {
      override fun onExpired(name: String) {
        println("onExpired: $name")
      }
    }).then()

    StepVerifier.create(setMono.concatWith(getMono).concatWith(listenerMono))
      .verifyComplete()

    Thread.sleep(5000)
  }

  @Test
  fun `deleteEventListener`() {
    val bucket = client!!.getBucket<String>("user:1:username")
    val setMono = bucket.set("test")
    // cli 로 delete 후 이벤트 동작 확인
    // del user:1:username
    val listenerMono = bucket.addListener(object: DeletedObjectListener {
      override fun onDeleted(name: String) {
        println("onDeleted: $name")
      }
    }).then()

    StepVerifier.create(setMono.concatWith(listenerMono))
      .verifyComplete()

    Thread.sleep(100000L)
  }
}