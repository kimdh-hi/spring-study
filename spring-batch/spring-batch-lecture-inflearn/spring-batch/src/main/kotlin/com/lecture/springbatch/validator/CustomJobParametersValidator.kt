package com.lecture.springbatch.validator

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.JobParametersValidator

class CustomJobParametersValidator: JobParametersValidator {
  override fun validate(parameters: JobParameters?) {
    parameters ?: throw throw JobParametersInvalidException("parameters cannot be null")

    if (parameters.isEmpty)
      throw JobParametersInvalidException("parameters cannot be empty")

    if (parameters.getString("name") == null)
      throw JobParametersInvalidException("name parameter cannot be null")
  }
}