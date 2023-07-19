package com.toy.jpafolder.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class FileFolderRepositoryImplTest @Autowired constructor(
  private val fileFolderRepository: FileFolderRepository
) {

  @Test
  fun `test`() {
    val result = fileFolderRepository.getList()

    result.forEach {
      println(it)
    }
  }

}