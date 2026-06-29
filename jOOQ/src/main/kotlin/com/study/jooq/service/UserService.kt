package com.study.jooq.service

import com.study.jooq.domain.User
import com.study.jooq.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserById(id: Long): User? {
        return userRepository.findById(id)
    }

    fun getUsersByTeamId(teamId: Long): List<User> {
        return userRepository.findByTeamId(teamId)
    }

    fun getUserByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    @Transactional
    fun createUser(user: User): User {
        return userRepository.save(user)
    }

    @Transactional
    fun updateUser(id: Long, user: User): User? {
        return userRepository.update(id, user)
    }

    @Transactional
    fun deleteUser(id: Long): Boolean {
        return userRepository.deleteById(id)
    }
}
