package com.study.infleanrestapi.vo

import java.time.LocalDateTime

data class EventVO (
    var name: String,
    var description: String,
    var beginEnrollmentDateTime: LocalDateTime, // 이벤트 등록일시
    var closeEnrollmentDateTime: LocalDateTime, // 이벤트 등록만료 일시
    var beginEventDateTime: LocalDateTime, // 이벤트 시작일시
    var endEventDateTime: LocalDateTime, // 이벤트 종료일시
    var location: String,
    var basePrice: Int,
    var maxPrice: Int,
    var limitOfEnrollment: Int, // 참가인원 제한
)