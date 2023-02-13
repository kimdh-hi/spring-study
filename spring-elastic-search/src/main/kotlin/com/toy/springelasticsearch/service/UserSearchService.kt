package com.toy.springelasticsearch.service

import com.toy.springelasticsearch.domain.User
import com.toy.springelasticsearch.repository.UserSearchRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UserSearchService(
  private val userSearchRepository: UserSearchRepository
) {

  fun searchByName(name: String, pageable: Pageable): List<User> {
    return userSearchRepository.searchByName(name, pageable)
  }
}