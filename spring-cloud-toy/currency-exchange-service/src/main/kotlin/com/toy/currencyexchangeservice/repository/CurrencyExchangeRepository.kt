package com.toy.currencyexchangeservice.repository

import com.toy.currencyexchangeservice.domain.CurrencyExchange
import org.springframework.data.jpa.repository.JpaRepository

interface CurrencyExchangeRepository: JpaRepository<CurrencyExchange, String> {

    fun findByFromAndTo(from: String, to: String): CurrencyExchange?
}