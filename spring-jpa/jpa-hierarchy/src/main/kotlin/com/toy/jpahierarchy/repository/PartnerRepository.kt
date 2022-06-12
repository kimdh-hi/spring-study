package com.toy.jpahierarchy.repository

import com.toy.jpahierarchy.domain.Partner
import org.springframework.data.repository.CrudRepository

interface PartnerRepository: CrudRepository<Partner, String>, PartnerRepositoryCustom {

}

interface PartnerRepositoryCustom {
  fun findPartner(id: String): Partner?
}