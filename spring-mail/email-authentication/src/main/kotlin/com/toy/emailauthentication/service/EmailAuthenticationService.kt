package com.toy.emailauthentication.service

import com.toy.emailauthentication.common.MailTemplates
import com.toy.emailauthentication.common.SesMailSender
import com.toy.emailauthentication.domain.EmailAuthentication
import com.toy.emailauthentication.domain.User
import com.toy.emailauthentication.repository.EmailAuthenticationRepository
import com.toy.emailauthentication.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.context.Context

@Service
@Transactional(readOnly = true)
class EmailAuthenticationService(
  private val emailAuthenticationRepository: EmailAuthenticationRepository,
  private val sesMailSender: SesMailSender,
  private val userRepository: UserRepository
) {

  @Transactional
  fun sendMail(username: String, mailTemplates: MailTemplates) {
    when (mailTemplates) {
      MailTemplates.SIGNUP -> sendSignupMail(username, MailTemplates.SIGNUP)
      MailTemplates.RESET_PASSWORD -> sendResetPasswordMail(username, MailTemplates.RESET_PASSWORD)
    }
  }

  fun sendSignupMail(username: String, mailTemplates: MailTemplates) {
    val user = userRepository.findByUsername(username) ?: throw IllegalArgumentException("user not found ...")
    val emailAuthentication = save(user)
    val context = Context()
    context.setVariable("redirect", emailAuthentication.id)

    sesMailSender.send(user.username, mailTemplates.subject, mailTemplates.templateName, context)
  }

  private fun sendResetPasswordMail(username: String, mailTemplates: MailTemplates) {
    val user = userRepository.findByUsername(username) ?: throw IllegalArgumentException("user not found ...")
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