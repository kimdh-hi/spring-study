package com.lecture.fsmysql.domain.post.service

import com.lecture.fsmysql.domain.post.entity.Timeline
import com.lecture.fsmysql.domain.post.repository.TimelineRepository
import org.springframework.stereotype.Service

@Service
class TimelineWriteService(
  private val timelineRepository: TimelineRepository
) {

  fun devliveryToTimeline(postId: Long, toMemberIds: List<Long>) {
    val timelines = toMemberIds
      .map { Timeline(memberId = it, postId = postId) }
    timelineRepository.bulkInsert(timelines)
  }
}