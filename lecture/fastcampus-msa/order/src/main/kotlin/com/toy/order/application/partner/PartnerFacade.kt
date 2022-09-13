package com.toy.order.application.partner

import com.toy.order.domain.notification.NotificationService
import com.toy.order.domain.partner.PartnerCommand
import com.toy.order.domain.partner.PartnerInfo
import com.toy.order.domain.partner.PartnerService
import org.springframework.stereotype.Service

@Service
class PartnerFacade(
  private val partnerService: PartnerService,
  private val notificationService: NotificationService
) {

  fun registerPartner(command: PartnerCommand): PartnerInfo {
    val partnerInfo = partnerService.registerPartner(command)
    notificationService.sendEmail(partnerInfo.email, "파트너 등록", description = "파트너 등록")
    return partnerInfo
  }
}