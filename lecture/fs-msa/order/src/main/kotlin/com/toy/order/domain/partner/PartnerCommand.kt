package com.toy.order.domain.partner

data class PartnerCommand(
  val partnerName: String,
  val businessNo: String,
  val email: String,
) {
  fun toEntity(): Partner {
    return Partner.new(
      partnerName = partnerName,
      businessNo = businessNo,
      email = email
    )
  }
}
