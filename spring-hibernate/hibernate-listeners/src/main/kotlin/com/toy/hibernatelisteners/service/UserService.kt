package com.toy.hibernatelisteners.service

import com.toy.hibernatelisteners.repository.UserRepository
import com.toy.hibernatelisteners.vo.UserSaveRequestVO
import com.toy.hibernatelisteners.vo.UserUpdateRequestVO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
  private val userRepository: UserRepository
) {

  fun save(requestVO: UserSaveRequestVO) = userRepository.save(requestVO.toEntity())


  fun update(id: String, requestVO: UserUpdateRequestVO) {
    val user = userRepository.findByIdOrNull(id) ?: throw RuntimeException("not found ...")
    user.update(requestVO.username, requestVO.password)
  }

  fun delete(id: String) {
    userRepository.deleteById(id)
  }

}