package com.study.swagger.vo

import io.swagger.v3.oas.annotations.media.Schema

data class RequestVO (
    @Schema(description = "Request description", example = "Example Data", required = true, minLength = 2, maxLength = 10)
    val data: String? = null
)