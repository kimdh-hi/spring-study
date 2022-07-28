package com.toy.currencyexchangeservice.domain

import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "currency_exchnage")
@Entity
class CurrencyExchange(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    var id: String? = null,

    @Column(name = "currency_from")
    var from: String,

    @Column(name = "currency_to")
    var to: String,

    var conversionMultiple: BigDecimal,

    var environment: String? = null
) {

    override fun toString(): String {
        return "CurrencyExchange(id=$id, from='$from', to='$to', conversionMultiple=$conversionMultiple, environment=$environment)"
    }
}