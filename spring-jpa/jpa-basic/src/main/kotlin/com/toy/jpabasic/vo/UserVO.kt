package com.toy.jpabasic.vo

import com.querydsl.core.annotations.QueryProjection
import com.toy.jpabasic.domain.Authority
import com.toy.jpabasic.domain.Company
import com.toy.jpabasic.domain.Role
import com.toy.jpabasic.domain.User

data class UserSaveRequestVO(
  val username: String,
  val authority: Authority
) {

  fun toEntity(company: Company) = User(username = username, company = company, role = authority.getRole())
}

data class UserResponseVO(
  val id: String,
  val username: String,
  val company: CompanyResponseVO,
) {
  companion object {
    fun of(user: User) =
      UserResponseVO(
        id = user.id!!,
        username = user.username,
        company = CompanyResponseVO.of(user.company)
      )
  }
}

data class UserListResponseVO(
  val id: String,
  val username: String,
  val authority: Authority
) {
  @QueryProjection constructor(
    id: String,
    username: String,
    authority: String
  ): this(id, username, Authority.valueOf(authority))
}

data class UserListV3ResponseVO(
  val id: String,
  val username: String,
  val authority: Authority
) {
  @QueryProjection constructor(
    id: String,
    username: String,
    role: Role
  ): this(id, username, Authority.valueOf(role.getAuthority().name))
}
