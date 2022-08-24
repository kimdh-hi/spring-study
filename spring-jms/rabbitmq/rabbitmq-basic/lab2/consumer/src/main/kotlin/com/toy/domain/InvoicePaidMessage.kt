package com.toy.domain

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class InvoicePaidMessage(
  val invoiceNumber: String,

  @JsonFormat(pattern = "yyyy-MM-dd")
  val paidDate: LocalDate,

  val paymentNumber: String
)