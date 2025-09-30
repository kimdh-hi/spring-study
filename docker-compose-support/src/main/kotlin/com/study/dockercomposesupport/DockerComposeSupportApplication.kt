package com.study.dockercomposesupport

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DockerComposeSupportApplication

fun main(args: Array<String>) {
  runApplication<DockerComposeSupportApplication>(*args)
}
