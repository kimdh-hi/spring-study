package com.toy.redissonbasic

import com.toy.redissonbasic.base.BaseTest
import org.junit.jupiter.api.Test
import org.redisson.api.RPatternTopicReactive
import org.redisson.api.RTopicReactive
import org.redisson.client.codec.StringCodec

class `11-PubSub`: BaseTest() {

  /**
   * [ publisher (redis-cli) ]
   * publish [channel-name] [message]
   * public test-room test-message
   * 구독중인 sub 의 수를 응답
   */

  @Test
  fun subscriber1() {
    val topic: RTopicReactive = client!!.getTopic("test-room", StringCodec.INSTANCE)
    topic.getMessages(String::class.java)
      .doOnError { println(it) }
      .doOnNext { println(it) }
      .subscribe()

    Thread.sleep(600_000)
  }

  @Test
  fun subscriber2() {
    val topic: RTopicReactive = client!!.getTopic("test-room", StringCodec.INSTANCE)
    topic.getMessages(String::class.java)
      .doOnError { println(it) }
      .doOnNext { println(it) }
      .subscribe()

    Thread.sleep(600_000)
  }

  @Test
  fun `subscriber3 - patternTopic`() {
    val topic: RPatternTopicReactive = client!!.getPatternTopic("test-room*", StringCodec.INSTANCE)
    topic.addListener(String::class.java) { pattern: CharSequence, topic: CharSequence, message: String ->
      println("$pattern:$topic => $message")
    }.subscribe()

    Thread.sleep(600_000)
  }
}