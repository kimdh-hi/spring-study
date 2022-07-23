package com.study.mapstruct.controller

import com.study.mapstruct.service.UserService
import com.study.mapstruct.vo.UserVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{id}")
    fun read(@PathVariable id: String): ResponseEntity<UserVO> {
        val responseVO = userService.read(id)

        return ResponseEntity.ok(responseVO)
    }
}