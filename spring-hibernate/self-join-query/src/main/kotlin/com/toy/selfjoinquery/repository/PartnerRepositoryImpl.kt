package com.toy.selfjoinquery.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.selfjoinquery.domain.Partner
import com.toy.selfjoinquery.domain.QPartner
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class PartnerRepositoryImpl(
  private val query: JPAQueryFactory
): PartnerRepositoryCustom, QuerydslRepositorySupportCustom(Partner::class.java) {

  override fun findAllV1(pageable: Pageable): Page<PartnerResponseVO> {

    return Page.empty()

  }
}