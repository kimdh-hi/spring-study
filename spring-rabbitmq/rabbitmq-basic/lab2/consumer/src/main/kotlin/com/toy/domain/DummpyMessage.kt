package com.toy.domain

/*
Producer - Consumer 패키지 이름이 같은지 확인

__TypeId__ : com.toy.domain.DummyMessage
 */
data class DummyMessage(
  val content: String,
  val publishId: Int
)