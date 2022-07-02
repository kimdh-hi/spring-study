package com.example.ex.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import org.apache.tomcat.jni.Local
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class DateRequest(
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    var startDate: LocalDateTime? = null
)
