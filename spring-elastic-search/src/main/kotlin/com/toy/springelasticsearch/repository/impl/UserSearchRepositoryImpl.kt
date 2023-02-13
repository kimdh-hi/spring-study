package com.toy.springelasticsearch.repository.impl

import com.toy.springelasticsearch.domain.User
import com.toy.springelasticsearch.repository.CustomUserSearchRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.query.Criteria
import org.springframework.data.elasticsearch.core.query.CriteriaQuery

class UserSearchRepositoryImpl(
  private val operations: ElasticsearchOperations
): CustomUserSearchRepository {

  override fun searchByName(name: String, pageable: Pageable): List<User> {
    val criteria = Criteria.where("name").contains(name)
    val query = CriteriaQuery(criteria, pageable)
    return operations.search(query, User::class.java)
      .map { it.content }
      .toList()
  }
}