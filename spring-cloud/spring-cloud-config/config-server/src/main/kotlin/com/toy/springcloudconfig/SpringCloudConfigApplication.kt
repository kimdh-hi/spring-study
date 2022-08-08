package com.toy.springcloudconfig

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer

@EnableConfigServer
@SpringBootApplication
class SpringCloudConfigApplication

 fun main(args: Array<String>) {
    runApplication<SpringCloudConfigApplication>(*args)
}

