package com.toy.springneo4j.service

import com.toy.springneo4j.repository.StudentRepository
import com.toy.springneo4j.vo.StudentCreateRequestVO
import org.springframework.stereotype.Service

@Service
class StudentService(
  private val studentRepository: StudentRepository
) {

  fun create(vo: StudentCreateRequestVO) {

  }
}