package com.toy.coroutinevsreactive.reactive.service

import com.toy.coroutinevsreactive.domain.User
import com.toy.coroutinevsreactive.reactive.repository.UserReactiveRepository
import com.toy.coroutinevsreactive.vo.UserResponseVO
import com.toy.coroutinevsreactive.vo.UserSaveRequestVO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class ReactiveMemberService(private val userReactiveRepository: UserReactiveRepository) {

  fun findByUsername(username: String): Mono<User> =
    userReactiveRepository.findByUsername(username)

  fun findById(id: Long): Mono<UserResponseVO> =
    userReactiveRepository.findById(id)
      .flatMap { user -> UserResponseVO.fromEntity(user)  }

  fun deleteByName(username: String): Mono<Unit> =
    userReactiveRepository
      .findByUsername(username)
      .map { user -> userReactiveRepository.deleteById(user.id!!) }

  fun save(requestVO: UserSaveRequestVO): Mono<UserResponseVO> =
      userReactiveRepository.existsByUsername(requestVO.username)
        .filter{ it == false }
        .flatMap { userReactiveRepository.save(requestVO.toEntity()) }
        .flatMap { UserResponseVO.fromEntity(it) }
}