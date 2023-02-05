package com.toy.springkafka.model

import com.fasterxml.jackson.annotation.JsonProperty

data class User(
  @get:JsonProperty("name")
  val name: String,
  @get:JsonProperty("age")
  val age: Int
)