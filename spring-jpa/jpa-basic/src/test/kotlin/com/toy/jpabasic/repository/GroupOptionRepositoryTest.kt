package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.Group
import com.toy.jpabasic.domain.GroupOption
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
class GroupOptionRepositoryTest(
  private val groupOptionRepository: GroupOptionRepository,
  private val groupRepository: GroupRepository,
  private val em: EntityManager
) {

  @Test
  fun save() {
    val group = Group(name = "test-gggg")
    val savedGroup = groupRepository.save(group)
    val groupOption = GroupOption.of(savedGroup, true)
    savedGroup.groupOption = groupOption

    em.flush()
    em.clear()

    groupOptionRepository.findAll().forEach {
      println(it)
    }
  }

  @Test
  fun delete() {
    val group = Group(name = "test-gggg")
    val savedGroup = groupRepository.save(group)
    val groupOption = GroupOption.of(savedGroup, true)
    savedGroup.groupOption = groupOption
    em.flush()
    em.clear()

    groupRepository.deleteById(group.id!!)
    em.flush()
    em.clear()

    groupOptionRepository.findAll().forEach {
      println(it)
    }
  }

  @Test
  fun test() {
    groupOptionRepository.findAll().forEach {
      println(it)
    }
  }
}