package com.toy.order.domain.partner

interface PartnerService {
  fun registerPartner(command: PartnerCommand): PartnerInfo
  fun getPartnerInfo(partnerToken: String): PartnerInfo
  fun enablePartner(partnerToken: String): PartnerInfo
  fun disablePartner(partnerToken: String): PartnerInfo
}