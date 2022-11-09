package com.toy.order.domain.partner

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PartnerServiceImpl(
  private val partnerStore: PartnerStore,
  private val partnerReader: PartnerReader
): PartnerService {

  @Transactional
  override fun registerPartner(command: PartnerCommand): PartnerInfo {
    val partner = command.toEntity()
    val storedPartner = partnerStore.store(partner)
    return PartnerInfo.of(storedPartner)
  }

  @Transactional(readOnly = true)
  override fun getPartnerInfo(partnerToken: String): PartnerInfo {
    val partner = partnerReader.getPartner(partnerToken)
    return PartnerInfo.of(partner)
  }

  @Transactional
  override fun enablePartner(partnerToken: String): PartnerInfo {
    val partner = partnerReader.getPartner(partnerToken)
    partner.enable()
    return PartnerInfo.of(partner)
  }

  @Transactional
  override fun disablePartner(partnerToken: String): PartnerInfo {
    val partner = partnerReader.getPartner(partnerToken)
    partner.disable()
    return PartnerInfo.of(partner)
  }
}