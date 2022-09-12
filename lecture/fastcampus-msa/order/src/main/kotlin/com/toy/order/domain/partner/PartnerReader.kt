package com.toy.order.domain.partner

interface PartnerReader {
  fun getPartner(partnerToken: String): Partner
}
