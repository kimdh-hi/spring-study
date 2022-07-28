package com.toy.conversion.currencyconversionmicroservice.vo

import com.toy.conversion.currencyconversionmicroservice.domain.CurrencyConversion
import java.math.BigDecimal

data class ExchangeResponseVO(
    var id: String,
    var from: String,
    var to: String,
    var conversionMultiple: BigDecimal,
    var environment: String? = null
) {

    fun toEntity(quantity: BigDecimal): CurrencyConversion {
        return CurrencyConversion(
            id = this.id,
            from = this.from,
            to = this.to,
            conversionMultiple = conversionMultiple,
            totalCaluatedAmount = quantity.multiply(conversionMultiple),
            environment = environment
        )
    }
}