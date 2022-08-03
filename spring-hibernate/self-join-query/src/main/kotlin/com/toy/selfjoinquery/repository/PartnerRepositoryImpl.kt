package com.toy.selfjoinquery.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.selfjoinquery.domain.Partner
import com.toy.selfjoinquery.domain.QPartner
import com.toy.selfjoinquery.domain.QPartner.partner
import com.toy.selfjoinquery.vo.PartnerResponseVO
import com.toy.selfjoinquery.vo.QPartnerResponseVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class PartnerRepositoryImpl(
  private val query: JPAQueryFactory
): PartnerRepositoryCustom, QuerydslRepositorySupportCustom(Partner::class.java) {

  override fun search(partnerId: String, pageable: Pageable): Page<PartnerResponseVO> {
    val childPartnerIds = mutableSetOf<String>()
    val directChildPartnerIds = getChildPartnerIds(partnerId)

    directChildPartnerIds.forEach {
      childPartnerIds.add(it)
      getChildPartnerIds(it, childPartnerIds as HashSet<String>)
    }

    val parentPartner = QPartner("parentPartner")
    val jpaQuery = query
      .from(partner)
      .leftJoin(partner.parentPartner, parentPartner)
      .where(partner.id.`in`(childPartnerIds))

    val selectClause = QPartnerResponseVO(partner.id, partner.name, parentPartner.id)

    return getPage(jpaQuery, selectClause, pageable)
  }

  private fun getChildPartnerIds(partnerId: String, childPartnerIds: HashSet<String>) {
    val returnPartnerIds = getChildPartnerIds(partnerId)
    returnPartnerIds.forEach {
      childPartnerIds.add(it)
      getChildPartnerIds(it, childPartnerIds)
    }
  }

  override fun getChildPartnerIds(partnerId: String): HashSet<String> {
    val childPartner = QPartner("childPartner")
    val childPartnerIdList = query.select(childPartner.id)
      .from(partner)
      .join(partner.childPartners, childPartner)
      .where(partner.id.eq(partnerId))
      .fetch()

    return childPartnerIdList.toHashSet()
  }
}