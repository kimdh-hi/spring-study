package com.toy.springjpatsid.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport



class QuerydslRepositorySupportCustom(domainClass: Class<*>): QuerydslRepositorySupport(domainClass) {

  fun getSlice(
    cursorId: String? = null,
    pageable: Pageable
  ) {

  }
}