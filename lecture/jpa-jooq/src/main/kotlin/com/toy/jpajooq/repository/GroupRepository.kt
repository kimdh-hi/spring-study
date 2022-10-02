package com.toy.jpajooq.repository

import com.toy.jpajooq.domain.Group
import org.springframework.data.repository.CrudRepository

interface GroupRepository: CrudRepository<Group, String> {
}