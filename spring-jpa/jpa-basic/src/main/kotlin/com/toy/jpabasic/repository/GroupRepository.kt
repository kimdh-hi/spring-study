package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.Group
import org.springframework.data.repository.CrudRepository

interface GroupRepository: CrudRepository<Group, String>