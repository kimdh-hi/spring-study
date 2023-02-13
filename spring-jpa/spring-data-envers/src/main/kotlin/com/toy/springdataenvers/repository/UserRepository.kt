package com.toy.springdataenvers.repository

import com.toy.springdataenvers.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.history.RevisionRepository
import java.time.LocalDateTime

interface UserRepository: JpaRepository<User, String>, RevisionRepository<User, String, Int>, UserRepositoryCustom {

}

interface UserRepositoryCustom {

  fun findRevisionsByIdAndCreatedDate(id: String, createdDate: LocalDateTime): List<User>
}