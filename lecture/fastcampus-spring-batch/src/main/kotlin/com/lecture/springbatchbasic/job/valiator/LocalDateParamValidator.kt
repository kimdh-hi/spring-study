package com.lecture.springbatchbasic.job.valiator

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.JobParametersValidator
import org.springframework.util.StringUtils
import java.time.LocalDate
import java.time.format.DateTimeParseException

class LocalDateParamValidator(
  private val parameterName: String
): JobParametersValidator {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun validate(parameters: JobParameters?) {
    parameters?.let {
      val localDate = parameters.getString(parameterName)
      if(!StringUtils.hasText(localDate))
        throw JobParametersInvalidException(parameterName + "cannot be null")

      try {
        LocalDate.parse(localDate)
      } catch (e: DateTimeParseException) {
        throw JobParametersInvalidException(parameterName + "should be date format")
      }
    }
  }
}