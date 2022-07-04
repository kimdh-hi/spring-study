package com.toy.noticeservice.repository

import com.toy.noticeservice.domain.Notice
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface NoticeRepository: CoroutineCrudRepository<Notice, Long> {
}