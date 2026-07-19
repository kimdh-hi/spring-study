package com.study.quartzscheduler.web

import com.study.quartzscheduler.dto.CronJobRequest
import com.study.quartzscheduler.service.CronJobService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cron-jobs")
class CronJobController(
  private val service: CronJobService,
) {
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun register(@RequestBody request: CronJobRequest) = service.register(request)
}
