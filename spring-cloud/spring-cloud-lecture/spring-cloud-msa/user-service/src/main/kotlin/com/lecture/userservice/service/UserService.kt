package com.lecture.userservice.service

import com.lecture.userservice.domain.User
import com.lecture.userservice.feign.OrderServiceClient
import com.lecture.userservice.repository.UserRepository
import com.lecture.userservice.vo.UserResponseVO
import com.lecture.userservice.vo.UserSaveRequestVO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository,
  private val passwordEncoder: PasswordEncoder,
  private val restTemplate: RestTemplate,
  private val orderServiceClient: OrderServiceClient
): UserDetailsService {

  override fun loadUserByUsername(username: String): UserDetails {
    val user = userRepository.findByEmail(username) ?: throw UsernameNotFoundException("user not found")
    return org.springframework.security.core.userdetails.User(
      user.email, user.password,
      true, true, true, true,
      arrayListOf()
    )
  }

  @Transactional
  fun save(vo: UserSaveRequestVO): UserResponseVO {
    val userEntity = vo.toEntity(passwordEncoder.encode(vo.password))
    return UserResponseVO.of(userRepository.save(userEntity))
  }

  fun findAll(): List<UserResponseVO> {
    return userRepository.findAll()
      .map { UserResponseVO.of(it) }
  }

  fun findById(id: String): UserResponseVO {
    val user = userRepository.findByIdOrNull(id)
      ?: throw RuntimeException("user not found")

//    val orderUrl = "http://ORDER_SERVICE/order-service/%s/orders"
//    val orderResponseVOList = restTemplate.exchange(
//      orderUrl, HttpMethod.GET, null, object : ParameterizedTypeReference<List<OrderResponseVO>>(){}
//    ).body ?: listOf()

    val orderResponseVOList = orderServiceClient.findOrderById(id)

    return UserResponseVO.of(user, orderResponseVOList)
  }

  fun findByUsername(username: String): User {
    return userRepository.findByEmail(username) ?: throw UsernameNotFoundException("user not found")
  }
}