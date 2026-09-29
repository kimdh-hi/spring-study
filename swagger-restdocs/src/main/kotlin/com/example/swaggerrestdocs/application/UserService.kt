package com.example.swaggerrestdocs.application

import com.example.swaggerrestdocs.domain.model.User
import com.example.swaggerrestdocs.domain.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class UserCommand(val name: String, val email: String)

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun findAll(): List<User> = userRepository.findAll()

    @Transactional(readOnly = true)
    fun findById(id: Long): User = userRepository.findByIdOrNull(id) ?: throw UserNotFoundException(id)

    @Transactional
    fun create(command: UserCommand): User {
        if (userRepository.existsByEmail(command.email)) throw DuplicateEmailException(command.email)
        return userRepository.save(User(command.name, command.email))
    }

    @Transactional
    fun update(id: Long, command: UserCommand): User {
        val user = findById(id)
        if (userRepository.existsByEmailAndIdNot(command.email, id)) throw DuplicateEmailException(command.email)
        user.name = command.name
        user.email = command.email
        return user
    }

    @Transactional
    fun delete(id: Long) {
        if (!userRepository.existsById(id)) throw UserNotFoundException(id)
        userRepository.deleteById(id)
    }
}

class UserNotFoundException(id: Long) : RuntimeException("User $id not found")
class DuplicateEmailException(email: String) : RuntimeException("Email $email already exists")
