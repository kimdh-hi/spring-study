package com.toy.emailauthentication.service

import com.toy.emailauthentication.common.MailTemplates
import com.toy.emailauthentication.common.SesMailSender
import com.toy.emailauthentication.domain.EmailAuthentication
import com.toy.emailauthentication.domain.User
import com.toy.emailauthentication.repository.EmailAuthenticationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.context.Context

@Service
@Transactional(readOnly = true)
class EmailAuthenticationService(
  private val emailAuthenticationRepository: EmailAuthenticationRepository,
  private val sesMailSender: SesMailSender
) {

  @Transactional
  fun sendMail(user: User, mailTemplates: MailTemplates) {
    when (mailTemplates) {
      MailTemplates.SIGNUP -> sendSignupMail(user, MailTemplates.SIGNUP)
      MailTemplates.RESET_PASSWORD -> sendResetPasswordMail(user, MailTemplates.RESET_PASSWORD)
    }
  }

  fun sendSignupMail(user: User, mailTemplates: MailTemplates) {
    val emailAuthentication = save(user)
    val context = Context()
    context.setVariable("redirect", emailAuthentication.id)

    sesMailSender.send(user.username, mailTemplates.subject, mailTemplates.templateName, context)
  }

  private fun sendResetPasswordMail(user: User, mailTemplates: MailTemplates) {
    val emailAuthentication = save(user)
    val context = Context()
    context.setVariable("redirect", emailAuthentication.id)

    sesMailSender.send(user.username, mailTemplates.subject, mailTemplates.templateName, context)
  }

  @Transactional
  fun save(user: User): EmailAuthentication {
    deleteAllByUsername(user.username)
    return emailAuthenticationRepository.save(EmailAuthentication.newInstance(user))
  }

  @Transactional
  fun deleteAllByUsername(username: String) {
    emailAuthenticationRepository.findByUsername(username).forEach {
      emailAuthenticationRepository.delete(it)
    }
  }

  @Transactional
  fun authenticate(id: String) {
    val emailAuthentication = emailAuthenticationRepository.findByIdOrNull(id)
      ?: throw IllegalArgumentException("invalid authenticate request ...")

    val user = emailAuthentication.user
    user.authenticate()

    deleteAllByUsername(user.username)
  }
}