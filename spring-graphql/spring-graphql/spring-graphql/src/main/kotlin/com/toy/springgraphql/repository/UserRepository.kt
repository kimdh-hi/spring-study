package com.toy.springgraphql.repository

import com.toy.springgraphql.domain.User
import org.springframework.data.repository.CrudRepository
import org.springframework.graphql.data.GraphQlRepository

@GraphQlRepository
interface UserRepository: CrudRepository<User, String>