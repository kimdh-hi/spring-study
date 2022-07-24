package com.example.ex

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExApplication

fun main(args: Array<String>) {
    runApplication<ExApplication>(*args)
}
