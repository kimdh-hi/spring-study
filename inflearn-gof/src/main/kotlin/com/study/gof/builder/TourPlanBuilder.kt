package com.study.gof.builder

import java.time.LocalDate

interface TourPlanBuilder {

    fun title(title: String): TourPlanBuilder

    fun nightsAndDays(nights: Int, days: Int): TourPlanBuilder

    fun startDate(date: LocalDate): TourPlanBuilder

    fun whereToStay(whereToStay: String): TourPlanBuilder

    fun addPlan(day: Int, plain: String): TourPlanBuilder

    fun getPlain(): TourPlan
}