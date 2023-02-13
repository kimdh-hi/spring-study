package com.toy.springelasticsearch.controller

import com.toy.springelasticsearch.domain.User
import com.toy.springelasticsearch.service.UserSearchService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserSearchController(
  private val userSearchService: UserSearchService
) {

  @GetMapping("/name/{name}")
  fun searchByName(@PathVariable name: String, pageable: Pageable): List<User> {
    return userSearchService.searchByName(name, pageable)
  }

}