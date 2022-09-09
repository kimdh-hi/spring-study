package com.toy.userservice.repository

import com.toy.userservice.domain.User
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface UserRepository: ReactiveCrudRepository<User, Int> {

  @Modifying
  @Query(
    """
      update tb_users
      set balance = balance - :amount
      where id = :userId
      and balance >= :amount
    """
  )
//    @Query(
//    "update tb_users " +
//    "set balance = balance - :amount " +
//    "where id = :userId " +
//    "and balance >= :amount"
//    )
  fun updateUserBalance(userId: Int, amount: Int): Mono<Boolean>
}