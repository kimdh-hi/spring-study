package com.study.gof.`04-builder`

import java.time.LocalDate

fun main() {
  val tourPlanBuilder = DefaultTourPlanBuilder()

  val tourPlan = tourPlanBuilder
    .title("title")
    .nightsAndDays(1, 2)
    .startDate(LocalDate.now().plusMonths(1))
    .whereToStay("stay")
    .addPlan(1, "plan1")
    .addPlan(2, "plan2")
    .addPlan(3, "plan3")
    .build() // 완전한 인스턴스를 생성하도록 서포트 (검증로직을 포함할 수 있음)

}