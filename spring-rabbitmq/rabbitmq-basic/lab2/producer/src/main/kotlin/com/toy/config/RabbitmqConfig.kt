package com.toy.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/*
Jackson2JsonMessageConverter 사용
- mq로 메시지 전송시 매번 objectMapper 를 이용해서 json 메시지로 변환하는 작업이 필요함.
- Jackson2JsonMessageConverter json 으로 변환하는 작업을 수행함.
- mq로 메시지 전송시 그냥 객체타입을 보내면 됨 (변환x)

Consumer 쪽에서 역직렬화 시에는 __TypeId__ 를 사용함
__TypeId__ 는 메시지로 보낸 객체의 풀패키지명임
Consumer 쪽에서는 __TypeId__ 가 동일한지 여부로 역직렬화 대상 객체를 찾음
즉, 패키지명이 같아야 함 (음...)
 */
@Configuration
class RabbitmqConfig {

  @Bean
  fun objectMapper() = JsonMapper
    .builder()
    .findAndAddModules()
    .build()

  @Bean
  fun converter(@Autowired objectMapper: ObjectMapper) = Jackson2JsonMessageConverter(objectMapper)
}