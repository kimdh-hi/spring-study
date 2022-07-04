package com.toy.noticeservice.repository

import com.toy.noticeservice.base.AbstractRepositoryTest
import com.toy.noticeservice.domain.Notice
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class NoticeRepositoryTest(
  var noticeRepository: NoticeRepository
): AbstractRepositoryTest() {

  @Test
  fun `save`() = runBlocking {
    //given
    val notice = Notice(title = "title", content = "content", userId = "user-01")

    //when
    val savedNotice = noticeRepository.save(notice)

    //then
    assertAll({
      assertNotNull(savedNotice.id)
    })
  }
}