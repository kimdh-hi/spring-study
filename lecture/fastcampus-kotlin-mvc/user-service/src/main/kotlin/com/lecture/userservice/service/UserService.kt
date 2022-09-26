package com.lecture.userservice.service

import com.lecture.userservice.domain.User
import com.lecture.userservice.exception.AlreadyExistsDataException
import com.lecture.userservice.model.SignupRequest
import com.lecture.userservice.model.SignupResponse
import com.lecture.userservice.repository.UserRepository
import com.lecture.userservice.utils.BCryptUtils
import org.springframework.stereotype.Service

@Service
class UserService(
  private val userRepository: UserRepository
) {

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
}