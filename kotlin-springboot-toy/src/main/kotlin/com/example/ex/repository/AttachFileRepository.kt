package com.example.ex.repository

import com.example.ex.domain.AttachFile
import org.springframework.data.jpa.repository.JpaRepository

interface AttachFileRepository : JpaRepository<AttachFile, Long> {
}