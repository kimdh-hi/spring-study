package com.toy.conversion.currencyconversionmicroservice.repository

import com.toy.conversion.currencyconversionmicroservice.domain.CurrencyConversion
import org.springframework.data.jpa.repository.JpaRepository

interface CurrencyConversionRepository: JpaRepository<CurrencyConversion, String> {
}