package com.study.readwritesplitting.ui

import com.study.readwritesplitting.application.UserService
import com.study.readwritesplitting.domain.user.repository.WhereAmI
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
  private val userService: UserService,
) {

  @PostMapping("/users")
  fun save(@RequestParam name: String): Map<String, Any> {
    val id = userService.save(name)
    return mapOf("id" to id, "name" to name)
  }

  @GetMapping("/users/where")
  fun whereOnRead(): Map<String, Any> = toMap("READ(readOnly tx)", userService.whereAmIOnRead())

  @GetMapping("/users/where-write")
  fun whereOnWrite(): Map<String, Any> = toMap("WRITE(tx)", userService.whereAmIOnWrite())

  @GetMapping("/users/count")
  fun count(): Map<String, Any> = mapOf("count" to userService.count())

  private fun toMap(routedBy: String, w: WhereAmI) =
    mapOf(
      "routedBy" to routedBy,
      "serverId" to w.getServerId(),
      "hostname" to w.getHostname(),
      "readOnly" to (w.getReadOnly() == 1),
    )
}
