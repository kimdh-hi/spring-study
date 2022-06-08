package com.toy.emailauthentication.common

enum class MailTemplates(val subject: String, val templateName: String) {

  SIGNUP("Signup authentication mail.", "signup_template"),
  RESET_PASSWORD("Reset Password authentication mail.", "reset_password_template")
}