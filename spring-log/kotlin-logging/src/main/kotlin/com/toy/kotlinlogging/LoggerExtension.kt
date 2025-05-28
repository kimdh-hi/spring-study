package com.toy.kotlinlogging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)

fun <A : Any> A.lazyLogger(): Lazy<Logger> = lazy { LoggerFactory.getLogger(this.javaClass) }

fun <A: Any> A.lazyKotlinLogging(): Lazy<KLogger> = lazy { KotlinLogging.logger { } }
