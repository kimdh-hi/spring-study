package com.toy.domain

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class InvoiceCreatedMessage(
  val amount: Int,

  @JsonFormat(pattern = "yyyy-MM-dd")
  val createdDate: LocalDate,

  val currency: String,

  val invoiceNumber: String
)