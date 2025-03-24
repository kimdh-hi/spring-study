package com.toy.springkotlin.repository

import com.toy.springkotlin.entity.Group
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class GroupRepositoryTest @Autowired constructor(
  private val groupRepository: GroupRepository,
  private val entityManager: EntityManager,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Test
  fun save() {
    val group = Group(name = "group")
    val savedGroup = assertDoesNotThrow { groupRepository.save(group) }

    log.debug("savedGroup={}", savedGroup)
  }

  @Test
  fun find() {
    val group = groupRepository.save(Group(name = "group"))
    val findGroup = groupRepository.findByIdOrNull(group.id.value)

    assertAll(
      { assertThat(findGroup).isNotNull },
      { assertThat(findGroup!!.name).isEqualTo(group.name) },
    )
  }

  @Test
  fun update() {
    val savedGroup = groupRepository.save(Group(name = "group"))
    entityManager.flush()
    entityManager.clear()

    val findGroup = groupRepository.findByIdOrNull(savedGroup.id.value)!!

    val updateGroupName = "updatedGroup"
    findGroup.name = updateGroupName
    entityManager.flush()
    entityManager.clear()

    val updatedGroup = groupRepository.findByIdOrNull(findGroup.id.value)
    assertThat(updatedGroup!!.name).isEqualTo(updateGroupName)
  }

  @Test
  fun findByIdCustom() {
    val savedGroup = groupRepository.save(Group(name = "group"))
    val findGroup = groupRepository.findByIdCustom(savedGroup.id)
    assertAll(
      { assertThat(findGroup).isNotNull },
      { assertThat(findGroup!!.id).isEqualTo(savedGroup.id) }
    )
  }
}
