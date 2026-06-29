package com.study.multitenancy.ui

import com.study.multitenancy.domain.model.User
import com.study.multitenancy.domain.repository.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userRepository: UserRepository) {

    @PostMapping
    fun create(@RequestBody request: CreateUserRequest): User {
        return userRepository.save(User(name = request.name, email = request.email))
    }

    @GetMapping
    fun findAll(): List<User> = userRepository.findAll()
}

data class CreateUserRequest(val name: String, val email: String)
