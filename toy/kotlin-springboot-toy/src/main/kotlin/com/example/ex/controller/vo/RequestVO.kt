package com.example.ex.controller.vo

import javax.validation.constraints.NotNull

data class RequestVO (
    @NotNull
    val data: String? = null
)
