package com.toy.reactivejdsl.vo

import com.toy.reactivejdsl.domain.User

data class UserSearchVO(
  val roleId: String?,
  val keyword: String?,
)

data class UserSaveRequestVO(
  val name: String,
  val username: String,
  val password: String,
  val roleId: String,
  val companyId: String
)

data class UserSaveResponseVO(
  val id: String,
  val name: String,
  val username: String,
) {
  companion object {
    fun of(user: User): UserSaveResponseVO {
      user.apply {
        return UserSaveResponseVO(id = id!!, name = name, username = username)
      }
    }
  }
}

data class UserResponseVO(
  val id: String,
  val name: String,
  val username: String,
  val company: String,
  val role: String
) {
  companion object {
    fun of(user: User): UserResponseVO =
      UserResponseVO(
        id = user.id!!,
        name = user.name,
        username = user.username,
        company = user.company.name,
        role = user.role.name!!
      )
  }
}