package com.toy.webfluxr2dbcpostgres

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound
import java.io.FilterInputStream

@SpringBootApplication
@ConfigurationPropertiesScan
class WebfluxR2dbcPostgresApplication

fun main(args: Array<String>) {
    runApplication<WebfluxR2dbcPostgresApplication>(*args)
}
