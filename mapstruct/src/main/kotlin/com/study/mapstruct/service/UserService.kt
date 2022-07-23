package com.study.mapstruct.service

import com.study.mapstruct.mapper.UserMapper
import com.study.mapstruct.repository.UserRepository
import com.study.mapstruct.vo.UserVO
import org.mapstruct.factory.Mappers
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class UserService(private val userRepository: UserRepository) {

    fun read(id: String): UserVO {
        val user = userRepository.findByIdOrNull(id) ?: throw RuntimeException("user not found ...")

        val mapper = Mappers.getMapper(UserMapper::class.java)
        return mapper.toVO(user)
    }
}