package com.toy.jpaeventlistener.service

import com.toy.jpaeventlistener.domain.UserInviteEmail
import com.toy.jpaeventlistener.domain.UserInviteEmailRepository
import com.toy.jpaeventlistener.domain.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserInviteService(
  private val userInviteEmailRepository: UserInviteEmailRepository,
  private val eventPublisher: ApplicationEventPublisher
) {

  @Transactional
  fun invite(vo: UserInviteRequestVO) {
    val userInviteEmail = userInviteEmailRepository.save(UserInviteEmail(email = vo.username))

    val inviteEmailSendEvent = UserInviteEmailSendEvent(userInviteEmail.email)
    eventPublisher.publishEvent(inviteEmailSendEvent)
  }
}

data class UserInviteRequestVO(
  val username: String
)

data class UserInviteEmailSendEvent(
  val username: String
)