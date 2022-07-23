package com.toy.limitsservice.controller

import com.toy.limitsservice.bean.Limits
import com.toy.limitsservice.common.CustomProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LimitsController(
    private val properties: CustomProperties
) {

    @GetMapping("/limits")
    fun get(): Limits {
        return Limits(minimum = properties.minimum, maximum = properties.maximun)
    }
}