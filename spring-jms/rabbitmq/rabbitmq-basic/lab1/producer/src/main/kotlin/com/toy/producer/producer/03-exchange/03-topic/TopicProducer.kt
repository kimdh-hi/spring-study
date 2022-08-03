package com.toy.producer.producer.`03-exchange`.`03-topic`

/*
Topic exchange

- 여러 조건의 routingKey 를 사용할 수 있음 (각 routing-key 는 .(점) 으로 구분)
- * : 한 개 단어를 대체
- # : 0개 이상의 단어를 대체

예제 시나리오
- Car 타입의 메시지를 발행
- topic key 구성
  - <brand>.<color>.<type>
  -
- *.white.*
  - 총 3개 단어로 구성되어 있고, 가운데가 white 인 routingKey 와 매칭
  - hyundai.white.sedan
- kia.#
  - 총 2개 이상의 단어로 구성되어 있고, 첫번째가 kia 인 routingKey 와 매칭
  - ex) kia.black.suv
- *.*.suv
  - 총 3개 단어로 구성되어 있고, 마지막이 suv 인 routingKey 와 매칭
  - ex) kia.blue.suv
 */
