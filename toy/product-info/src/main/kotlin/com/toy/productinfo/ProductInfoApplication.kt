package com.toy.productinfo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProductInfoApplication

fun main(args: Array<String>) {
  runApplication<ProductInfoApplication>(*args)
}
