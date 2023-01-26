package com.lecture.userservice.vo

import com.lecture.userservice.domain.User
import java.time.LocalDateTime
import java.util.UUID

data class UserSaveRequestVO(
  val name: String,
  val email: String,
  val password: String
) {
  fun toEntity(encPassword: String) = User(
      email = email,
      name = name,
      password = encPassword
  )
}

data class UserResponseVO(
  val id: String?,
  val name: String,
  val email: String,
  val createdDate: LocalDateTime?,
  val updatedDate: LocalDateTime?,
  val orders: List<OrderResponseVO> = listOf()
) {
  companion object {
    fun of(user: User, orders: List<OrderResponseVO> = listOf()) = UserResponseVO(
      id = user.id,
      name = user.name,
      email = user.email,
      createdDate = user.createdDate,
      updatedDate = user.updatedDate,
      orders = orders
    )
  }
}