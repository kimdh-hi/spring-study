package com.toy.dpopproofjwt.ui.item

import com.toy.dpopproofjwt.application.item.ItemService
import com.toy.dpopproofjwt.domain.model.Item
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
@RequestMapping("/api/items")
class ItemController(
  private val itemService: ItemService,
) {

  @GetMapping
  fun list(): List<ItemResponse> = itemService.list().map(::ItemResponse)

  @GetMapping("/{id}")
  fun get(@PathVariable id: Long): ItemResponse = ItemResponse(itemService.get(id))

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun create(@RequestBody request: ItemRequest): ItemResponse = ItemResponse(itemService.create(request.name))

  @PutMapping("/{id}")
  fun update(@PathVariable id: Long, @RequestBody request: ItemRequest): ItemResponse =
    ItemResponse(itemService.update(id, request.name))

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable id: Long) = itemService.delete(id)
}

data class ItemRequest(
  val name: String,
)

data class ItemResponse(
  val id: Long,
  val name: String,
) {
  constructor(item: Item) : this(item.id!!, item.name)
}
