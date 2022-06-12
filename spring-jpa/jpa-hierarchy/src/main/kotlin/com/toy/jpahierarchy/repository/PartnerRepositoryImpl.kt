package com.toy.jpahierarchy.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.jpahierarchy.domain.Partner
import com.toy.jpahierarchy.domain.QPartner
import com.toy.jpahierarchy.domain.QPartner.partner

class PartnerRepositoryImpl(private val query: JPAQueryFactory): PartnerRepositoryCustom {

  override fun findPartner(id: String): Partner? {
    return query.selectFrom(partner)
      .where(partner.id.eq(id))
      .fetchOne()
  }
}