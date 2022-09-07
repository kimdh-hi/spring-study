package com.toy.userservice.service

import com.toy.userservice.dto.UserDto
import com.toy.userservice.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserService(
  private val userRepository: UserRepository
) {

  fun getAll(): Flux<UserDto> = userRepository.findAll()
    .map { UserDto.of(it) }

  fun get(id: Int): Mono<UserDto> = userRepository.findById(id)
    .map { UserDto.of(it) }

  fun save(userDtoMono: Mono<UserDto>): Mono<UserDto> =
    userDtoMono
      .map { it.toEntity() }
      .flatMap { userRepository.save(it) }
      .map { UserDto.of(it) }

  fun update(id: Int, userDtoMono: Mono<UserDto>): Mono<UserDto> =
    userRepository.findById(id)
      .flatMap {
        userDtoMono
          .map { it.toEntity() }
          .doOnNext { it.id = id }
      }
      .flatMap { userRepository.save(it) }
      .map { UserDto.of(it) }

  fun delete(id: Int): Mono<Void> = userRepository.deleteById(id)
}