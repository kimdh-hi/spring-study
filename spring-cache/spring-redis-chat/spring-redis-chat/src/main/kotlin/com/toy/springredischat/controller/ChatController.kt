package com.toy.springredischat.controller

import com.toy.springredischat.config.RedisMessageListener
import com.toy.springredischat.vo.MessageVO
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.web.bind.annotation.*
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/chat")
class ChatController(
  private val redisMessageListenerContainer: RedisMessageListenerContainer,
  private val redisTemplate: RedisTemplate<String, Any>,
  private val messageListener: RedisMessageListener
) {

  val channels: ConcurrentHashMap<String, ChannelTopic> = ConcurrentHashMap()

  @GetMapping("/channels")
  fun getChannelList(): ConcurrentHashMap.KeySetView<String, ChannelTopic> {
    return channels.keys
  }

  @PostMapping("/channels/{channelId}")
  fun createChannel(@PathVariable channelId: String): String {
    val channelTopic = ChannelTopic(channelId)
    redisMessageListenerContainer.addMessageListener(messageListener, channelTopic)
    channels[channelId] = channelTopic
    return "create channel success channelId: $channelId"
  }

  @PostMapping("/channels/{channelId}/chat")
  fun sendMessage(@PathVariable channelId: String, @RequestBody message: MessageVO) {
    val channel = channels[channelId] ?: throw IllegalArgumentException("channel not found")
    redisTemplate.convertAndSend(channel.topic, message)
  }
}