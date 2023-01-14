package com.lecture.inflearnspringsecurityoauth2.model.social

data class Attributes(
  val mainAttributes: Map<String, Any> = mapOf(),
  val subAttributes: Map<String, Any> = mapOf(),
  val otherAttributes: Map<String, Any> = mapOf()
)