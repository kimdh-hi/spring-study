package com.toy.springevent.event

import com.toy.springevent.event.command.UserWelcomeMailSendCommand
import com.toy.springevent.service.MailSendLogService
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase

@Component
class UserEventListener(
  private val mailSendLogService: MailSendLogService,
) {

  private val logger = LoggerFactory.getLogger(UserEventListener::class.java)

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
//  @Transactional(propagation = Propagation.REQUIRES_NEW)
  fun handle(command: UserWelcomeMailSendCommand) {
    logger.info("UserEventListener.UserWelcomeMailSendCommand username=${command.username}")
    logger.info("mailSend to ${command.username}...")

    if (command.eventException) {
      throw RuntimeException("UserWelcomeMailSendCommand ex...")
    }
    mailSendLogService.save(true)
  }
}
