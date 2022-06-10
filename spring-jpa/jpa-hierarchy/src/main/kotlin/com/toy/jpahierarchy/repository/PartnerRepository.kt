package com.toy.jpahierarchy.repository

import com.toy.jpahierarchy.domain.Partner
import org.springframework.data.repository.CrudRepository

interface PartnerRepository: CrudRepository<Partner, String> {

}