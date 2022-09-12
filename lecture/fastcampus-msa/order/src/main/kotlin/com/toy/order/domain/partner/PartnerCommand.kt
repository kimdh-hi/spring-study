package com.toy.order.domain.partner

data class PartnerCommand(
  val partnerName: String,
  val businessNo: String,
  val email: String,
) {
  fun toEntity(): Partner {
    return Partner.newPartner(
      partnerName = partnerName,
      businessNo = businessNo,
      email = email
    )
  }
}
