package com.toy.springevent.event

import com.toy.springevent.event.command.UserWelcomeMailSendCommand
import com.toy.springevent.service.MailSendLogService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener

@Component
class UserEventListener(
  private val mailSendLogService: MailSendLogService,
) {

  private val logger = LoggerFactory.getLogger(UserEventListener::class.java)

  @TransactionalEventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  fun handle(command: UserWelcomeMailSendCommand) {
    logger.info("UserEventListener.UserWelcomeMailSendCommand username=${command.username}")
    logger.info("mailSend to ${command.username}...")
    mailSendLogService.save(true)
  }
}
