package com.toy.springdataenvers.domain.listener

import com.toy.springdataenvers.domain.User
import com.toy.springdataenvers.repository.UserSomeData3Repository
import jakarta.persistence.PreRemove
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
class UserJpaListener {

  private val log = LoggerFactory.getLogger(javaClass)

  @Lazy
  @Autowired
  lateinit var userSomeData3Repository: UserSomeData3Repository

  @PreRemove
  fun preRemove(user: User) {
    userSomeData3Repository.findByUserId(user.id!!)?.let {
      userSomeData3Repository.delete(it)
    }
  }
}