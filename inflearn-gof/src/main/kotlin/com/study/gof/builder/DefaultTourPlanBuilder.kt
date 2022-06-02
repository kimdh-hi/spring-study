package com.study.gof.builder

import java.time.LocalDate

class DefaultTourPlanBuilder: TourPlanBuilder {


    override fun title(title: String): TourPlanBuilder {
        TODO("Not yet implemented")
    }

    override fun nightsAndDays(nights: Int, days: Int): TourPlanBuilder {
        TODO("Not yet implemented")
    }

    override fun startDate(date: LocalDate): TourPlanBuilder {
        TODO("Not yet implemented")
    }

    override fun whereToStay(whereToStay: String): TourPlanBuilder {
        TODO("Not yet implemented")
    }

    override fun addPlan(day: Int, plain: String): TourPlanBuilder {
        TODO("Not yet implemented")
    }

    override fun getPlain(): TourPlan {
        TODO("Not yet implemented")
    }
}