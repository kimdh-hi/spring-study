package com.study.gof.builder

import java.time.LocalDate

class DefaultTourPlanBuilder: TourPlanBuilder {

    private var title: String = ""
    private var nights: Int = 0
    private var days: Int = 0
    private var startDate: LocalDate = LocalDate.now()
    private var whereToStay: String = ""
    private var plans: MutableList<Plan> = mutableListOf()



    override fun title(title: String): TourPlanBuilder {
        this.title = title
        return this
    }

    override fun nightsAndDays(nights: Int, days: Int): TourPlanBuilder {
        this.nights = nights
        this.days = days
        return this
    }

    override fun startDate(startDate: LocalDate): TourPlanBuilder {
        this.startDate = startDate
        return this
    }

    override fun whereToStay(whereToStay: String): TourPlanBuilder {
        this.whereToStay = whereToStay
        return this
    }

    override fun addPlan(day: Int, plain: String): TourPlanBuilder {
        plans.add(Plan(day, plain))
        return this
    }

    override fun build(): TourPlan {
        return TourPlan(
            title = title,
            nights = nights,
            startDate = startDate,
            days = days,
            whereToStay = whereToStay,
            plan = plans
        )
    }
}