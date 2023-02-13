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
- color:white, material:wood // color가 white 이고 material이 wood 인 경우 q.promotion.discount 로 전달
- color:red, material:steel // color가 red 이고 material이 steel 인 경우 q.promotion.discount 로 전달

q.promotion.free-delivery Arguments
- color:red, material:wood, x-match:any // color가 red 이거나 material이 wood 인경우 q.promotion.free-delivery로 전달

x-match
- all: 모든 key-value 와 매칭여부 검사 후 해당 큐로 전달 (각 key-value 간 and 조건)
- any: 한 개 key-value 와 매칭시 해당 큐로 전달        (각 ke-value 간 or 조건)
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