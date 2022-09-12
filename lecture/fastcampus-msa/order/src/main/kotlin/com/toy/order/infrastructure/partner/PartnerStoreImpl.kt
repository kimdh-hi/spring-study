package com.toy.order.infrastructure.partner

import com.toy.order.domain.partner.Partner
import com.toy.order.domain.partner.PartnerStore

class PartnerStoreImpl(
  private val partnerRepository: PartnerRepository
): PartnerStore {

  override fun store(partner: Partner): Partner {
    return partnerRepository.save(partner)
  }
}