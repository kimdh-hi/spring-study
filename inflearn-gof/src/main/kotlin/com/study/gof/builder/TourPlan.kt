package com.study.gof.builder

import java.time.LocalDate

class TourPlan (
    var title: String,
    var nights: Int,
    var days: Int,
    var startDate: LocalDate,
    var whereToStay: String,
    var plan: MutableList<Plan>
) {
    override fun toString(): String {
        return "TourPlan(title='$title', nights=$nights, days=$days, startDate=$startDate, whereToStay='$whereToStay', plan=$plan)"
    }
}

class Plan (
    var day: Int,
    var plan: String
) {
    override fun toString(): String {
        return "Plan(day=$day, plan='$plan')"
    }
}