package com.toy.jpaeventlistener.service

import com.toy.jpaeventlistener.domain.UserParticipantRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserParticipantService(
  private val userParticipantRepository: UserParticipantRepository
) {

  @Transactional
  fun deleteByUserId(userId: String) {
    userParticipantRepository.findByUserId(userId)?.let {
      userParticipantRepository.delete(it)
    }
  }
}