package com.study.presignedurl.domain.repository

import com.study.presignedurl.domain.model.FileObject
import org.springframework.data.jpa.repository.JpaRepository

interface FileObjectRepository : JpaRepository<FileObject, Long>
