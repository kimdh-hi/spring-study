package com.toy.springintegrationdemo.constants

enum class MqttTopic(val format: String) {
  DEFAULT("/default"),
  TEST("/test"),
  SINGLE_LEVEL_TEST("/test/+"),
  MULTI_LEVEL_TEST("/test/#")
}