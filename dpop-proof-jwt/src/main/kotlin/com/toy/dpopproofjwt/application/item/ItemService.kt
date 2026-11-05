package com.toy.dpopproofjwt.application.item

import com.toy.dpopproofjwt.common.exception.NotFoundException
import com.toy.dpopproofjwt.domain.model.Item
import com.toy.dpopproofjwt.domain.repository.ItemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ItemService(
  private val itemRepository: ItemRepository,
) {

  @Transactional(readOnly = true)
  fun list(): List<Item> = itemRepository.findAll()

  @Transactional(readOnly = true)
  fun get(id: Long): Item = find(id)

  fun create(name: String): Item = itemRepository.save(Item(name))

  fun update(id: Long, name: String): Item = find(id).apply { this.name = name }

  fun delete(id: Long) = itemRepository.delete(find(id))

  private fun find(id: Long): Item =
    itemRepository.findByIdOrNull(id) ?: throw NotFoundException("Item $id not found.")
}
