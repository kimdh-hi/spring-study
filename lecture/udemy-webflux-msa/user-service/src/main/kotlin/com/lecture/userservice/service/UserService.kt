package com.lecture.userservice.service

import com.lecture.userservice.dto.UserDto
import com.lecture.userservice.repository.UserRepository
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

  fun save(dto: Mono<UserDto>): Mono<UserDto> = dto
    .map { it.toEntity() }
    .flatMap { userRepository.save(it) }
    .map { UserDto.of(it) }

  fun update(id: Int, dto: Mono<UserDto>): Mono<UserDto> = dto
    .map { userDto ->
      userRepository.findById(id)
        .map { user -> user.update(userDto.name, userDto.balance) }
    }
    .flatMap { it.flatMap { userRepository.save(it) } }
    .map { UserDto.of(it) }

  fun delete(id: Int): Mono<Void> = userRepository.deleteById(id)
}