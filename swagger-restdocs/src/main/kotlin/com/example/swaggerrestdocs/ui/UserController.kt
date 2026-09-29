package com.example.swaggerrestdocs.ui

import com.example.swaggerrestdocs.application.UserCommand
import com.example.swaggerrestdocs.application.UserService
import com.example.swaggerrestdocs.domain.model.User
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping
    fun findAll(): List<UserResponse> = userService.findAll().map(User::toResponse)

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): UserResponse = userService.findById(id).toResponse()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: UserRequest): UserResponse =
        userService.create(request.toCommand()).toResponse()

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: UserRequest): UserResponse =
        userService.update(id, request.toCommand()).toResponse()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = userService.delete(id)
}

data class UserRequest(
    @field:NotBlank val name: String,
    @field:Email @field:NotBlank val email: String,
) {
    fun toCommand() = UserCommand(name, email)
}

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
)

private fun User.toResponse() = UserResponse(id = requireNotNull(id), name = name, email = email)
