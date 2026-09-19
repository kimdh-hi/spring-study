package com.toy.springrawwebsocket.infra.redis.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import java.util.concurrent.Executors

@Configuration
class RedisListenerConfig {
  @Bean
  fun redisMessageListenerContainer(connectionFactory: RedisConnectionFactory): RedisMessageListenerContainer =
    RedisMessageListenerContainer().apply {
      setConnectionFactory(connectionFactory)
      setTaskExecutor(Executors.newVirtualThreadPerTaskExecutor())
      setSubscriptionExecutor(Executors.newVirtualThreadPerTaskExecutor())
    }
}
