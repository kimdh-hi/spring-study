package com.toy.order.application.partner

import com.toy.order.domain.notification.NotificationService
import com.toy.order.domain.partner.PartnerCommand
import com.toy.order.domain.partner.PartnerInfo
import com.toy.order.domain.partner.PartnerService
import org.springframework.stereotype.Service

/*
GoF 의 Facade 패턴에서 따온 네이밍이다.
(서브 모듈에 대한 인터페이스의 의존성을 모아서 클라이언트가 여러 서브 시스템을 사용하게 쉽게 함)

각기 다른 도메인의 service 를 갖고 있다.
 */
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