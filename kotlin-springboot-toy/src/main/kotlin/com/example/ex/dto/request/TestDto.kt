package com.example.ex.dto.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class TestDto (
    @field:NotBlank
    var data: String? = null
)