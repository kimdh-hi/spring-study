package com.lecture.springbatchbasic.job

import com.lecture.springbatchbasic.base.AbstractBatchBaseTest
import com.lecture.springbatchbasic.base.BatchTestConfig
import com.lecture.springbatchbasic.core.domain.PlainText
import com.lecture.springbatchbasic.core.repository.PlainTextRepository
import com.lecture.springbatchbasic.core.repository.ResultTextRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.stream.IntStream

@SpringBootTest(classes = [PlainTextJobConfig::class, BatchTestConfig::class])
internal class PlainTextJobConfigTest(
  private val plainTextRepository: PlainTextRepository,
  private val resultTextRepository: ResultTextRepository
): AbstractBatchBaseTest() {

  @AfterEach
  fun clean() {
    plainTextRepository.deleteAll()
    resultTextRepository.deleteAll()
  }

  @Test
  fun `성공`() {
    //given
    getPlainTexts(10)

    //when
    val result = jobLauncherTestUtils.launchJob()

    //then
    assertAll({
      assertEquals(ExitStatus.COMPLETED, result.exitStatus)
      assertEquals(10, resultTextRepository.count())
    })
  }

  private fun getPlainTexts(count: Int) = IntStream.range(0, count)
    .forEach { plainTextRepository.save(PlainText(text = "text$it")) }

  @Test
  fun `plainText가 없는 경우 resultText도 없다`() {
    //given
    //when
    val result = jobLauncherTestUtils.launchJob()

    //then
    assertAll({
      assertEquals(ExitStatus.COMPLETED, result.exitStatus)
      assertEquals(resultTextRepository.count(), 0)
    })
  }
}