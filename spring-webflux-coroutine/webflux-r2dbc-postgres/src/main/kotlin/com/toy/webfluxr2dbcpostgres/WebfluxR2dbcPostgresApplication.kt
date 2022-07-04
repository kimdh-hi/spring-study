package com.toy.webfluxr2dbcpostgres

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class WebfluxR2dbcPostgresApplication {
//    init {
//        val allowBlockingCall = BlockHoundIntegration { builder ->
//            builder
//                .allowBlockingCallsInside("java.util.UUID", "randomUUID")
//                .allowBlockingCallsInside("java.io.InputStream", "readNBytes")
//                .allowBlockingCallsInside("java.io.FilterInputStream", "read")
//        }
//        BlockHound.install(CoroutinesBlockHoundIntegration(), allowBlockingCall)
//    }
}

fun main(args: Array<String>) {
    runApplication<WebfluxR2dbcPostgresApplication>(*args)
}
