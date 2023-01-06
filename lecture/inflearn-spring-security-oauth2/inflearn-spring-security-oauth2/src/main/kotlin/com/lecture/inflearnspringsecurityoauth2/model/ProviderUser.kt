package com.lecture.inflearnspringsecurityoauth2.model

import org.springframework.security.core.GrantedAuthority

interface ProviderUser {

  fun getId(): String

  fun getUsername(): String

  fun getEmail(): String

  fun getPassword(): String

  fun getProvider(): String

  fun getAuthorities(): List<GrantedAuthority>

  fun getAttributes(): Map<String, Any>
}