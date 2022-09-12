package com.toy.order.infrastructure.partner

import com.toy.order.domain.partner.Partner
import com.toy.order.domain.partner.PartnerReader

class PartnerReaderImpl(
  private val partnerRepository: PartnerRepository
): PartnerReader {

  override fun getPartner(partnerToken: String): Partner {
    return partnerRepository.findPartnerByPartnerToken(partnerToken)
      ?: throw RuntimeException("partner not found")
  }
}