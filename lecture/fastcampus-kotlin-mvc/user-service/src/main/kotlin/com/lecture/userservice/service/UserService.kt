package com.lecture.userservice.service

import com.lecture.userservice.config.JwtProperties
import com.lecture.userservice.domain.User
import com.lecture.userservice.exception.AlreadyExistsDataException
import com.lecture.userservice.exception.NotFoundException
import com.lecture.userservice.exception.UsernameOrPasswordNotMatchedException
import com.lecture.userservice.model.SignInRequest
import com.lecture.userservice.model.SignInResponse
import com.lecture.userservice.model.SignupRequest
import com.lecture.userservice.repository.UserRepository
import com.lecture.userservice.utils.BCryptUtils
import com.lecture.userservice.utils.JwtClaim
import com.lecture.userservice.utils.JwtUtils
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class UserService(
  private val userRepository: UserRepository,
  private val jwtProperties: JwtProperties,
  private val cacheManager: CoroutineCacheManager<User>
) {

  companion object {
    private val CACHE_TTL = Duration.ofMinutes(1)
  }

  suspend fun signup(request: SignupRequest) = with(request) {
    userRepository.findByUsername(request.username)?.let {
      throw AlreadyExistsDataException()
    }
    val user = User(
      username = username,
      password = BCryptUtils.encode(password),
      name = name
    )
    userRepository.save(user)
  }

  suspend fun signIn(request: SignInRequest): SignInResponse =
    with(userRepository.findByUsername(request.username) ?: throw NotFoundException("[signup] user not found...")) {
      val verified = BCryptUtils.verify(request.password, password)
      if (!verified)
        throw UsernameOrPasswordNotMatchedException()
      val jwtClaim = JwtClaim(
        userId = id!!,
        username = username,
        name = name,
        profileUrl = profileUrl
      )
      val jwtToken = JwtUtils.createToken(jwtClaim, jwtProperties)
      cacheManager.put(key = jwtToken, value = this, ttl = CACHE_TTL)
      SignInResponse(
        token = jwtToken
      )
    }

  suspend fun logout(token: String) {
    cacheManager.evict(token)
  }
}