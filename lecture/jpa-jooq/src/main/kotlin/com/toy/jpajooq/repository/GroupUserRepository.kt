package com.toy.jpajooq.repository

import com.toy.jpajooq.domain.GroupUser
import org.springframework.data.repository.CrudRepository

interface GroupUserRepository: CrudRepository<GroupUser, String> {
}