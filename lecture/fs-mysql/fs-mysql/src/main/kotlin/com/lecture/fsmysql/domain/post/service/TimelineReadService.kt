package com.lecture.fsmysql.domain.post.service

import com.lecture.fsmysql.common.CursorRequest
import com.lecture.fsmysql.common.PageCursor
import com.lecture.fsmysql.domain.post.entity.Timeline
import com.lecture.fsmysql.domain.post.repository.TimelineRepository
import org.springframework.stereotype.Service

@Service
class TimelineReadService(
  private val timelineRepository: TimelineRepository
) {

  fun getTimelines(memberId: Long, cursorRequest: CursorRequest): PageCursor<Timeline> {
    val timelines = findAllBy(memberId, cursorRequest)
    val nextKey = timelines.minOfOrNull { it.id } ?: CursorRequest.DEFAULT_KEY
    return PageCursor(cursorRequest.next(nextKey), timelines)
  }

  private fun findAllBy(memberId: Long, cursorRequest: CursorRequest): List<Timeline> {
    return if(cursorRequest.hasKey())
      timelineRepository.findAllByLessThanIdAndMemberIdAndOrderByIdDesc(cursorRequest.key!!, memberId, cursorRequest.size)
    else
      timelineRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size)
  }
}