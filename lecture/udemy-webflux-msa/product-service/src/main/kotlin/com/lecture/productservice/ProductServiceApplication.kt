package com.lecture.productservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
class ProductServiceApplication

fun main(args: Array<String>) {
  runApplication<ProductServiceApplication>(*args)
}
