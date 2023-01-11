package com.lecture.inflearnspringsecurityoauth2.config

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import java.util.*

class CustomAuthorityMapper: GrantedAuthoritiesMapper {

  companion object {
    const val ROLE_PREFIX = "ROLE_"
  }

  override fun mapAuthorities(authorities: MutableCollection<out GrantedAuthority>): MutableCollection<out GrantedAuthority> {
    val mapped = HashSet<GrantedAuthority>(authorities.size)
    for (authority in authorities) {
      mapped.add(mapAuthority(authority.authority))
    }
    return mapped
  }

  private fun mapAuthority(name: String): GrantedAuthority {
    var authority = name

    if(name.lastIndexOf(".") > 0) {
      val idx = name.lastIndexOf(".")
      authority = "SCOPE_${name.substring(idx + 1)}"
    }

    if(!name.startsWith(ROLE_PREFIX)) {
      authority = "${ROLE_PREFIX}name"
    }

    return SimpleGrantedAuthority(authority)
  }
}