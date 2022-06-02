package com.study.gof.builder

import java.time.LocalDate

class TourPlan (
    var title: String,
    var nights: Int,
    var days: Int,
    var startDate: LocalDate,
    var whereToStay: String,
    var plan: MutableList<Plan>
)

class Plan (
    var day: Int,
    var plan: String
)