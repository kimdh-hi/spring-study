package com.toy.springelasticsearch.repository

import com.toy.springelasticsearch.domain.User
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface UserSearchRepository: ElasticsearchRepository<User, String>, CustomUserSearchRepository

interface CustomUserSearchRepository {
  fun searchByName(name: String, pageable: Pageable): List<User>
}