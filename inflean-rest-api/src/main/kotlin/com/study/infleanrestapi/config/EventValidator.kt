package com.study.infleanrestapi.config

import com.study.infleanrestapi.vo.EventVO
import org.springframework.stereotype.Component
import org.springframework.validation.BindingResult

@Component
class EventValidator {

    fun validate(eventVO: EventVO, bindingResult: BindingResult) {
        if (eventVO.basePrice!! > eventVO.maxPrice!! && eventVO.maxPrice != 0) {
            bindingResult.rejectValue("maxPrice", "error-code", "maxPrice must bigger then basePrice when maxPrice equals zero.")
        }
    }
}