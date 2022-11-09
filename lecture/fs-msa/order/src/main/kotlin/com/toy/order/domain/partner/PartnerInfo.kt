package com.toy.order.domain.partner

data class PartnerInfo(
  var id: Long,
  val partnerToken: String,
  val partnerName: String,
  val businessNo: String,
  val email: String,
  var status: Partner.Status,
) {
  companion object {
    fun of(partner: Partner): PartnerInfo {
      return PartnerInfo(
        id = partner.id!!,
        partnerToken = partner.partnerToken,
        partnerName = partner.partnerName,
        businessNo = partner.businessNo,
        email = partner.email,
        status = partner.status
      )
    }
  }
}
