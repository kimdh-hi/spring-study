package com.toy.currencyexchangeservice.controller

import com.toy.currencyexchangeservice.domain.CurrencyExchange
import com.toy.currencyexchangeservice.repository.CurrencyExchangeRepository
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RequestMapping("/currency-exchange")
@RestController
class CurrencyExchangeController(
    private val currencyExchangeRepository: CurrencyExchangeRepository,
    private val env: Environment) {

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/from/{from}/to/{to}")
    fun exchange(
        @PathVariable from: String,
        @PathVariable to: String): CurrencyExchange? {

        val port = env.getProperty("local.server.port")
        val currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to)?.let {
            it.environment = port
            it
        } ?: throw IllegalArgumentException("cannot find $from to $to ...")

        log.info("currency-exchange: {}", currencyExchange)

        return currencyExchange
    }
}