package com.toy.jpaeventlistener.handler

import com.toy.jpaeventlistener.domain.UserInviteEmailRepository
import com.toy.jpaeventlistener.service.UserInviteEmailSendEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class EmailSendEventHandler(
  private val userInviteEmailRepository: UserInviteEmailRepository
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Transactional
  fun sendUserInviteEmailAfterCommit(event: UserInviteEmailSendEvent) {
    val userInviteEmail = userInviteEmailRepository.findByEmail(event.username) ?: throw RuntimeException("not found...")

    log.info("[EmailSendEventHandler.after-commit].sendUserInviteEmail userInviteEmail: {}", userInviteEmail)
    log.info("[EmailSendEventHandler.after-commit].sendUserInviteEmail event: {}", event)
  }

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  @Transactional
  fun sendUserInviteEmailBeforeCommit(event: UserInviteEmailSendEvent) {
    val userInviteEmail = userInviteEmailRepository.findByEmail(event.username) ?: throw RuntimeException("not found...")

    log.info("[EmailSendEventHandler.before-commit].sendUserInviteEmail userInviteEmail: {}", userInviteEmail)
    log.info("[EmailSendEventHandler.before-commit].sendUserInviteEmail event: {}", event)
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
  @Transactional
  fun sendUserInviteEmailAfterCompletion(event: UserInviteEmailSendEvent) {
    val userInviteEmail = userInviteEmailRepository.findByEmail(event.username) ?: throw RuntimeException("not found...")

    log.info("[EmailSendEventHandler.after-completion].sendUserInviteEmail userInviteEmail: {}", userInviteEmail)
    log.info("[EmailSendEventHandler.after-completion].sendUserInviteEmail event: {}", event)
  }
}