package com.toy.reactivejdsl.vo

import com.toy.reactivejdsl.common.BaseVO
import com.toy.reactivejdsl.domain.Authority
import com.toy.reactivejdsl.domain.User
import java.io.Serial
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UserSearchVO(
  val roleId: String?,
  val keyword: String?,
):BaseVO() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -4736358757706626084L
  }
}

data class UserSaveRequestVO(
  @field:NotBlank
  val name: String,
  @field:NotBlank
  val username: String,
  @field:NotBlank
  val password: String,
  @field:NotNull
  val roleId: String,
  @field:NotBlank
  val companyId: String
): BaseVO() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -2718219591572864706L
  }

}

data class UserSaveResponseVO(
  @field:NotBlank
  val id: String,
  @field:NotBlank
  val name: String,
  @field:NotBlank
  val username: String,
): BaseVO() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 1609165576765273762L

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
): BaseVO() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -7310309931005952434L

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

data class LoginRequestVO(
  @field:NotBlank
  val username: String,
  @field:NotBlank
  val password: String
)

data class LoginResponseVO(
  val token: String
)

data class SignupRequestVO(
  @field:NotBlank
  val username: String,
  @field:NotBlank
  val password: String
)