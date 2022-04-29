package com.study.springdownload.repository

import com.study.springdownload.domain.AttachFile
import org.springframework.data.jpa.repository.JpaRepository

interface AttachFileRepository: JpaRepository<AttachFile, Long> {
}