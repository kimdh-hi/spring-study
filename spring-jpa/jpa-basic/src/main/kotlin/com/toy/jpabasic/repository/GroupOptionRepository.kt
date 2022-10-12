package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.GroupOption
import org.springframework.data.repository.CrudRepository

interface GroupOptionRepository: CrudRepository<GroupOption, String> {
}