package com.toy.emailauthentication.repository

import com.toy.emailauthentication.domain.EmailAuthentication
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface EmailAuthenticationRepository: CrudRepository<EmailAuthentication, String> {

  @Query("select e from EmailAuthentication e join e.user u where u.username = :username")
  fun findByUsername(username: String): List<EmailAuthentication>
}