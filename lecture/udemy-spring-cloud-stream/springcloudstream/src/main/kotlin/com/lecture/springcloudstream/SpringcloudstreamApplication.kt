package com.lecture.springcloudstream

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.lecture.springcloudstream.\${sec}"])
class SpringcloudstreamApplication

fun main(args: Array<String>) {
	runApplication<SpringcloudstreamApplication>(*args)
}
