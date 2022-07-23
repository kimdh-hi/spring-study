package com.toy.coroutinevsreactive.coroutine.service

import com.toy.coroutinevsreactive.coroutine.repository.CoroutineUserRepository
import com.toy.coroutinevsreactive.coroutine.vo.UserResponseVO
import com.toy.coroutinevsreactive.coroutine.vo.UserSaveRequestVO
import kotlinx.coroutines.delay
import org.springframework.stereotype.Service

@Service
class CoroutineUserService(private val coroutineUserRepository: CoroutineUserRepository) {

  suspend fun findByUsername(username: String): UserResponseVO  {
    return coroutineUserRepository.findByUsername(username)?.let {
      UserResponseVO.fromEntity(it)
    } ?: throw IllegalArgumentException("user not found ...")
  }

  suspend fun findById(id: Long): UserResponseVO =
    coroutineUserRepository.findById(id)?.let {
      UserResponseVO.fromEntity(it)
    } ?: throw IllegalArgumentException("user not found ...")

  suspend fun deleteByName(username: String): Unit =
    coroutineUserRepository.findByUsername(username)?.let {
      coroutineUserRepository.deleteById(it.id!!)
    } ?: throw IllegalArgumentException("user not found ...")


  suspend fun save(requestVO: UserSaveRequestVO): UserResponseVO {
    if(coroutineUserRepository.existsByUsername(requestVO.username))
      throw IllegalArgumentException("user not found ...")

    val user = coroutineUserRepository.save(requestVO.toEntity())
    return UserResponseVO.fromEntity(user)
  }
}