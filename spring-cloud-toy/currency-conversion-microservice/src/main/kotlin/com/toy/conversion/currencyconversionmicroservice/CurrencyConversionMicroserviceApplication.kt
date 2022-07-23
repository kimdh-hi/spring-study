package com.toy.conversion.currencyconversionmicroservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class CurrencyConversionMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<CurrencyConversionMicroserviceApplication>(*args)
}
