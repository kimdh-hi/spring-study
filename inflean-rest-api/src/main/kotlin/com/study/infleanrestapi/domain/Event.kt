package com.study.infleanrestapi.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Event (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var description: String,
    var beginEnrollmentDateTime: LocalDateTime, // 이벤트 등록일시
    var closeEnrollmentDateTime: LocalDateTime, // 이벤트 등록만료 일시
    var beginEventDateTime: LocalDateTime, // 이벤트 시작일시
    var endEventDateTime: LocalDateTime, // 이벤트 종료일시
    var location: String? =  null, // 장소 (Optional)
    var basePrice: Int,
    var maxPrice: Int,
    var limitOfEnrollment: Int, // 참가인원 제한
    var offline: Boolean = false,
    var free: Boolean = false,
    @Enumerated(EnumType.STRING)
    var eventStatus: EventStatus,
) {

    companion object {
        fun newInstance (name: String, description: String,
                         beginEnrollmentDateTime: LocalDateTime, closeEnrollmentDateTime: LocalDateTime,
                         beginEventDateTime: LocalDateTime, endEventDateTime: LocalDateTime,
                         location: String, basePrice: Int, maxPrice: Int, limitOfEnrollment: Int, eventStatus: EventStatus,
                        offline: Boolean, free: Boolean): Event {

            return Event (
                name = name, description = description,
                beginEnrollmentDateTime = beginEnrollmentDateTime, closeEnrollmentDateTime = closeEnrollmentDateTime, endEventDateTime = endEventDateTime,
                beginEventDateTime = beginEventDateTime, location = location, basePrice = basePrice, maxPrice = maxPrice,
                limitOfEnrollment = limitOfEnrollment, eventStatus = eventStatus,
                offline = offline, free = free)
        }
    }
}