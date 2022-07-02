package com.toy.conversion.currencyconversionmicroservice.service

import com.toy.conversion.currencyconversionmicroservice.vo.ExchangeResponseVO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

//@FeignClient(name = "currency-exchange", url = "localhost:8000")
@FeignClient(name = "currency-exchange")
interface CurrencyExchangeService {

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    fun exchange(@PathVariable from: String, @PathVariable to: String): ExchangeResponseVO
}