package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.Role
import org.springframework.data.repository.CrudRepository

interface RoleRepository: CrudRepository<Role, String> {
}