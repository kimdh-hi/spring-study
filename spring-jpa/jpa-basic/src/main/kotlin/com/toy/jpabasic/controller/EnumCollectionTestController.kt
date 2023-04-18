package com.toy.jpabasic.controller

import com.toy.jpabasic.domain.EnumCollectionTestEntity
import com.toy.jpabasic.repository.EnumCollectionTestEntityRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.DayOfWeek
import java.util.EnumSet

@RestController
@RequestMapping("/enum-collection")
class EnumCollectionTestController(
  private val enumCollectionTestEntityRepository: EnumCollectionTestEntityRepository
) {

  @PostMapping
  fun save(@RequestBody vo: EnumCollectionTestVO) = enumCollectionTestEntityRepository.save(vo.toEntity())

  @GetMapping("/{id}")
  fun get(@PathVariable id: String) = enumCollectionTestEntityRepository.findByIdOrNull(id)
}

data class EnumCollectionTestVO(
  val id: String? = null,
  var weeks: List<String> = mutableListOf()
) {
  fun toEntity() = EnumCollectionTestEntity(
    weeks = EnumSet.copyOf(weeks.map { DayOfWeek.valueOf(it) })
  )
}