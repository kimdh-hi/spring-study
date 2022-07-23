package com.toy.jpabasic.common.constants

import com.toy.jpabasic.domain.Authority
import com.toy.jpabasic.domain.Role

object AuthorityConstants {

  val ROLE_ADMIN = Role("role-01", "관리자", authorities = setOf(Authority.ADMIN, Authority.USER))
  val ROLE_USER = Role("role-02", "사용자", authorities = setOf(Authority.USER))
}