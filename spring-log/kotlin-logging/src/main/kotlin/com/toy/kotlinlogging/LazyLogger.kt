package com.toy.kotlinlogging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.slf4j.Logger
import org.slf4j.LoggerFactory

// private val log by lazyLogger()
fun <A : Any> A.lazyLogger(): Lazy<Logger> = lazy { LoggerFactory.getLogger(this.javaClass) }

fun <A: Any> A.lazyKotlinLogging(): Lazy<KLogger> = lazy { KotlinLogging.logger { } }
