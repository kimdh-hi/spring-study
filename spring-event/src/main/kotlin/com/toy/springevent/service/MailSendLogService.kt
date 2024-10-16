package com.toy.springevent.service

import com.toy.springevent.domain.MailSendLog
import com.toy.springevent.domain.MailSendLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MailSendLogService(
  private val mailSendLogRepository: MailSendLogRepository,
) {

  @Transactional
  fun save(success: Boolean) {
    mailSendLogRepository.save(MailSendLog.of(success))
  }
}
