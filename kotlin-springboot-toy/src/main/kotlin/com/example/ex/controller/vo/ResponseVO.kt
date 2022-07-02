package com.example.ex.controller.vo

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ResponseVO (
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    var date: LocalDateTime
)