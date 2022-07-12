package com.toy.reactivejdsl.service

import com.toy.reactivejdsl.domain.Role
import com.toy.reactivejdsl.domain.User
import com.toy.reactivejdsl.repository.CommonCrudRepository
import com.toy.reactivejdsl.repository.CompanyRepository
import com.toy.reactivejdsl.repository.UserRepository
import com.toy.reactivejdsl.repository.get
import com.toy.reactivejdsl.security.JwtUtil
import com.toy.reactivejdsl.vo.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UserService(
  private val userRepository: UserRepository,
  private val companyRepository: CompanyRepository,
  private val jwtUtil: JwtUtil,
  private val commonCurdRepository: CommonCrudRepository<User, String>
) {

  suspend fun search(pageable: Pageable, searchVO: UserSearchVO): Page<UserResponseVO> = userRepository.search(pageable, searchVO)

  suspend fun save(requestVO: UserSaveRequestVO): UserSaveResponseVO {
    val company = companyRepository.findById(requestVO.companyId) ?: throw RuntimeException("company not found...")
    val user = User.newUser(
      name = requestVO.name,
      username = requestVO.username,
      password = requestVO.password,
      company = company,
      role = Role(id = requestVO.roleId)
    )

    val savedUser = userRepository.save(user)
    return UserSaveResponseVO.of(savedUser)
  }

  suspend fun get(id: String): UserResponseVO {
    val user = commonCurdRepository.get(id) ?: throw RuntimeException("user not found...")
    println(user)

//    val user = userRepository.findById(id) ?: throw RuntimeException("user not found...")
    return UserResponseVO.of(user)
  }

  suspend fun findUserByUsername(username: String) = userRepository.findByUsername(username)

  suspend fun login(requestVO: LoginRequestVO): String {
    val user = userRepository.findByUsername(requestVO.username) ?: throw IllegalArgumentException("user not found ...")
    user.checkPassword(requestVO.password)

    return jwtUtil.createToken(user)
  }
}