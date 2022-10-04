package com.lecture.inflearndatajpa.repository

import com.lecture.inflearndatajpa.domain.Study
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class StudyRepositoryTest(
  private val studyRepository: StudyRepository
) {

  @Test
  fun genericRepositoryTest() {
    //given
    val savedStudy = studyRepository.save(Study(name = "jpa"))

    //when
    val result = studyRepository.contains(savedStudy)

    //then
    assertTrue(result)
  }
}