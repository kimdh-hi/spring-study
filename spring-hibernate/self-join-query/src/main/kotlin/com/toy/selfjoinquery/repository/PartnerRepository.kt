package com.toy.selfjoinquery.repository

import com.toy.selfjoinquery.domain.Partner
import com.toy.selfjoinquery.vo.PartnerResponseVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface PartnerRepository: CrudRepository<Partner, String>, PartnerRepositoryCustom

interface PartnerRepositoryCustom {
  fun findAllV1(pageable: Pageable): Page<PartnerResponseVO>
}