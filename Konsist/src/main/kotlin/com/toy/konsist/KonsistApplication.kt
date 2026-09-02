package com.toy.konsist

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KonsistApplication

fun main(args: Array<String>) {
    runApplication<KonsistApplication>(*args)
}
