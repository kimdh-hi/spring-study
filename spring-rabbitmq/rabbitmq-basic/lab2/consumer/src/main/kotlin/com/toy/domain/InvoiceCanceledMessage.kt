package com.toy.domain

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class InvoiceCanceledMessage(
  @JsonFormat(pattern = "yyyy-MM-dd")
  val cancelDate: LocalDate,
  val invoiceNumber: String,
  val reason: String
)