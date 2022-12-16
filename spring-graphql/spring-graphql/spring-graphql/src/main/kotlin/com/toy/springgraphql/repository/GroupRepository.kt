package com.toy.springgraphql.repository

import com.toy.springgraphql.domain.Group
import org.springframework.data.repository.CrudRepository
import org.springframework.graphql.data.GraphQlRepository

@GraphQlRepository
interface GroupRepository: CrudRepository<Group, String>