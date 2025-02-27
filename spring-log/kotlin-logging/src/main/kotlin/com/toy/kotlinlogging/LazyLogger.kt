package com.toy.kotlinlogging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

// private val log by lazyLogger()
fun <A : Any> A.lazyLogger(): Lazy<Logger> = lazy { LoggerFactory.getLogger(this.javaClass) }
