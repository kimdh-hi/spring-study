package com.toy.jpafolder.repository

import com.toy.jpafolder.domain.File
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository: JpaRepository<File, String> {
}