package com.toy.conversion.currencyconversionmicroservice.controller

import com.toy.conversion.currencyconversionmicroservice.domain.CurrencyConversion
import com.toy.conversion.currencyconversionmicroservice.repository.CurrencyConversionRepository
import com.toy.conversion.currencyconversionmicroservice.service.CurrencyExchangeService
import com.toy.conversion.currencyconversionmicroservice.vo.ExchangeResponseVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

@RequestMapping("/currency-conversion")
@RestController
class ConversionController(
    private val currencyConversionRepository: CurrencyConversionRepository,
    private val currencyExchangeService: CurrencyExchangeService
){

    @GetMapping("/from/{from}/to/{to}/quantity/{quantity}")
    fun conversion(
        @PathVariable from: String, @PathVariable to: String,
        @PathVariable quantity: BigDecimal
    ): CurrencyConversion {

        val restTemplate = RestTemplate()
        val responseEntity = restTemplate.getForEntity(
            "http://localhost:8000/currency-exchange/from/$from/to/$to",
            ExchangeResponseVO::class.java,
        )

        val exchangeResponseVO = responseEntity.body!!

        return exchangeResponseVO.toEntity(quantity)
    }

    @GetMapping("/feign/from/{from}/to/{to}/quantity/{quantity}")
    fun conversionFeign(
        @PathVariable from: String, @PathVariable to: String,
        @PathVariable quantity: BigDecimal
    ): CurrencyConversion {
        val exchangeResponseVO = currencyExchangeService.exchange(from = from, to = to)
        return exchangeResponseVO.toEntity(quantity)
    }
}