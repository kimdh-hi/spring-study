package com.study.springdownload

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class SpringDownloadApplication

fun main(args: Array<String>) {
    runApplication<SpringDownloadApplication>(*args)
}
