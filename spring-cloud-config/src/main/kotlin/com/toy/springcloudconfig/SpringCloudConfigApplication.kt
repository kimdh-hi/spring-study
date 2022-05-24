package com.toy.springcloudconfig

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringCloudConfigApplication

fun main(args: Array<String>) {
    runApplication<SpringCloudConfigApplication>(*args)
}
