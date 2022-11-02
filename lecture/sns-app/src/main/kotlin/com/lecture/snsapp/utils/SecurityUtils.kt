package com.lecture.snsapp.utils

import com.lecture.snsapp.domain.User
import com.lecture.snsapp.exception.ApplicationException
import com.lecture.snsapp.exception.ErrorCode
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {

  fun getPrincipal(): User {
    val authentication = SecurityContextHolder.getContext().authentication
    return if (authentication.principal is User) {
      authentication.principal as User
    } else if(authentication.details is User) {
      authentication.details as User
    } else {
      throw ApplicationException(ErrorCode.INVALID_AUTHENTICATION)
    }
  }
}