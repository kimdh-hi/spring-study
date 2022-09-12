package com.toy.order.infrastructure.partner

import com.toy.order.domain.partner.Partner
import org.springframework.data.jpa.repository.JpaRepository

interface PartnerRepository: JpaRepository<Partner, Long> {

  fun findPartnerByPartnerToken(partnerToken: String): Partner?
}