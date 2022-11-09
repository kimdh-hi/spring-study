package com.toy.order.infrastructure.partner

import com.toy.order.common.exception.BaseException
import com.toy.order.common.exception.ErrorCodes
import com.toy.order.domain.partner.Partner
import com.toy.order.domain.partner.PartnerReader
import org.springframework.stereotype.Component

@Component
class PartnerReaderImpl(
  private val partnerRepository: PartnerRepository
): PartnerReader {

  override fun getPartner(partnerToken: String): Partner {
    return partnerRepository.findPartnerByPartnerToken(partnerToken)
      ?: throw BaseException(ErrorCodes.NOT_FOUND, "partner not found")
  }
}