package com.toy.springr2dbc.service

import com.toy.springr2dbc.domain.User
import com.toy.springr2dbc.r2dbc.UserR2dbcRepository
import com.toy.springr2dbc.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.coroutines.CoroutineContext

@Service
class UserService(
  private val userRepository: UserRepository,
  private val userR2dbcRepository: UserR2dbcRepository
) {

  @Transactional(readOnly = true, transactionManager = "transactionManager")
  fun findAllByJpa(): List<User> = userRepository.findAll()

  suspend fun findAllByR2dbc() = withContext(Dispatchers.IO) {
    val usersDeferred = async { userR2dbcRepository.findAll() }
    usersDeferred.await()
  }
}