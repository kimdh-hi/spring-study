package com.study.quartzscheduler.dto

data class CronJobRequest(
  val name: String,
  val cronExpression: String,
  val prompt: String,
)
