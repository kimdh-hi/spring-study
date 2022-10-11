package com.toy.springcore.event

import org.springframework.context.ApplicationEvent

/**
 * spring 4.2부터 ApplicationEvent 를 상속받지 않아도 된다.
 */
//data class MyEvent(
//  private val data: String
//): ApplicationEvent(data)

data class MyEvent(
  private val data: String
)