package com.study.springasync.service

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService(
  private val mailService: MailService,
) {

  fun signup(name: String) {
    UserIdHolder.set("user")
    SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken("test", "test")
    mailService.send("welcome.")
  }
}

object UserIdHolder {
  private val holder = ThreadLocal<String?>()

  fun get() = holder.get()
  fun set(value: String) = holder.set(value)
  fun clear() = holder.remove()
}
