package com.toy.jpafolder.service

import com.toy.jpafolder.repository.FileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FileService(
  private val fileRepository: FileRepository
) {

  @Transactional
  fun save() {

  }
}