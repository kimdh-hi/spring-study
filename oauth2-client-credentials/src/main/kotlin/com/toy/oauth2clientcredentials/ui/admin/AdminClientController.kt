package com.toy.oauth2clientcredentials.ui.admin

import com.toy.oauth2clientcredentials.application.client.ClientInfo
import com.toy.oauth2clientcredentials.application.client.ClientService
import com.toy.oauth2clientcredentials.application.client.CreateClientCommand
import com.toy.oauth2clientcredentials.application.client.CreatedClient
import com.toy.oauth2clientcredentials.domain.model.ClientAuthMethod
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/clients")
class AdminClientController(
  private val clientService: ClientService,
) {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun create(@RequestBody request: CreateClientRequest): CreatedClient =
    clientService.create(
      CreateClientCommand(request.name, request.scopes, request.authMethod, request.publicKey),
    )

  @GetMapping
  fun findAll(): List<ClientInfo> = clientService.findAll()

  @GetMapping("/{clientId}")
  fun find(@PathVariable clientId: String): ClientInfo = clientService.find(clientId)

  @DeleteMapping("/{clientId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable clientId: String) = clientService.delete(clientId)
}

data class CreateClientRequest(
  val name: String,
  val scopes: Set<String>,
  val authMethod: ClientAuthMethod = ClientAuthMethod.SECRET,
  val publicKey: String? = null,
)
