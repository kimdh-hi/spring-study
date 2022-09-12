package com.toy.order.domain.partner

import org.springframework.stereotype.Component

@Component
interface PartnerStore {
  fun store(partner: Partner): Partner
}
