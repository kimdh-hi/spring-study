package com.toy.order.infrastructure.partner

import com.toy.order.common.exception.BaseException
import com.toy.order.common.exception.ErrorCodes
import com.toy.order.domain.partner.Partner
import com.toy.order.domain.partner.PartnerStore
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component

@Component
class PartnerStoreImpl(
  private val partnerRepository: PartnerRepository
): PartnerStore {

  override fun store(partner: Partner): Partner {
    if(StringUtils.isEmpty(partner.partnerName)) throw BaseException(ErrorCodes.INVALID_PARAMETER, "invalid partnerName")
    if(StringUtils.isEmpty(partner.partnerToken)) throw BaseException(ErrorCodes.INVALID_PARAMETER, "invalid partnerToken")
    if(StringUtils.isEmpty(partner.businessNo)) throw BaseException(ErrorCodes.INVALID_PARAMETER, "invalid businessNo")
    if(StringUtils.isEmpty(partner.email)) throw BaseException(ErrorCodes.INVALID_PARAMETER, "invalid email")

    return partnerRepository.save(partner)
  }
}