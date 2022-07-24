package com.toy.reactivejdsl.service

import com.toy.reactivejdsl.domain.Company
import com.toy.reactivejdsl.domain.Role
import com.toy.reactivejdsl.domain.User
import com.toy.reactivejdsl.repository.CommonQuery
import com.toy.reactivejdsl.repository.query.UserQuery
import com.toy.reactivejdsl.repository.command.UserRepository
import com.toy.reactivejdsl.repository.get
import com.toy.reactivejdsl.repository.query.CompanyQuery
import com.toy.reactivejdsl.security.JwtUtil
import com.toy.reactivejdsl.vo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UserService(
  private val userQuery: UserQuery,
  private val companyQuery: CompanyQuery,
  private val userRepository: UserRepository,
  private val jwtUtil: JwtUtil,
  private val commonQuery: CommonQuery<User, String>
) {

  suspend fun search(pageable: Pageable, searchVO: UserSearchVO): Page<UserResponseVO> = userQuery.search(pageable, searchVO)

  suspend fun save(requestVO: UserSaveRequestVO): UserSaveResponseVO {
    val company = companyQuery.findById(requestVO.companyId) ?: throw RuntimeException("company not found...")
    val user = User.newUser(
      name = requestVO.name,
      username = requestVO.username,
      password = requestVO.password,
      company = Company(id="comp-01", name = "comp01"),
      role = Role(id = requestVO.roleId)
    )

    val savedUser = withContext(Dispatchers.IO) {
      userRepository.save(user)
    }
    return UserSaveResponseVO.of(savedUser)
  }



  suspend fun get(id: String): UserResponseVO {
    val user = userQuery.get(id) ?: throw RuntimeException("user not found...")
    return UserResponseVO.of(user)
  }

  suspend fun getV2(id: String): UserResponseVO {
    val user = commonQuery.get(id) ?: throw RuntimeException("user not found...")
    return UserResponseVO.of(user)
  }

  suspend fun findUserByUsername(username: String) = userQuery.findByUsername(username)

  suspend fun login(requestVO: LoginRequestVO): String {
    val user = userQuery.findByUsername(requestVO.username) ?: throw IllegalArgumentException("user not found ...")
    user.checkPassword(requestVO.password)

    return jwtUtil.createToken(user)
  }
}