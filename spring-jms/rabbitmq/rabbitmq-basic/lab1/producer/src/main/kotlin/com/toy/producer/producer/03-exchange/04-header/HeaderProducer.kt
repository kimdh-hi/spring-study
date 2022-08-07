package com.toy.producer.producer.`03-exchange`.`04-header`

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.producer.domain.Furniture
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

/*
Header Exchange

exchange 의 Arguments 를 통해 큐 식별
- Arguments 에 key-value 로 매핑
- MessageProperties 로 header 구성
  - amqp.core.MessageProperties

q.promotion.discount
q.promotion.free-delivery

x.promotion (header type exchange)

q.promotion.discount Arguments
- color:white, material:wood
- color:red, material:steel

q.promotion.free-delivery Arguments
- color:red, material:wood, x-match:any
 */
@Service
class HeaderProducer(
  private val rabbitTemplate: RabbitTemplate,
  private val objectMapper: ObjectMapper
) {

  fun sendMessage(furniture: Furniture) {
    val json = objectMapper.writeValueAsString(furniture)
    val messageProperties = MessageProperties()
    messageProperties.setHeader("color", furniture.color)
    messageProperties.setHeader("material", furniture.material)

    val message = Message(json.toByteArray(), messageProperties)
    rabbitTemplate.convertAndSend("x.promotion", "", message)
  }
}