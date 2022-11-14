package com.lecture.passbatch.job.pass

import com.lecture.passbatch.config.TestBatchConfig
import com.lecture.passbatch.domain.Pass
import com.lecture.passbatch.domain.enums.PassStatus
import com.lecture.passbatch.repository.PassRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
@ContextConfiguration(classes = [ExpirePassesJobConfig::class, TestBatchConfig::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class ExpirePassesJobConfigTest(
  private val jobLauncherTestUtils: JobLauncherTestUtils,
  private val passRepository: PassRepository
) {

  @Test
  fun expirePassesStep() {
    //given
    addPassTestData(10)

    //when
    val jobExecution = jobLauncherTestUtils.launchJob()
    val jobInstance = jobExecution.jobInstance

    //then
    assertEquals(ExitStatus.COMPLETED, jobExecution.exitStatus)
    assertEquals("expirePassesJob",jobInstance.jobName)
  }

  private fun addPassTestData(size: Int) {
    val now = LocalDateTime.now()
    val random = Random()

    val passes = mutableListOf<Pass>()
    for (i in 0 until  size) {
      val pass = Pass(
        packageSeq = 1,
        userId = "A10000$i",
        status = PassStatus.IN_PROGRESS,
        remainingCount = random.nextInt(11),
        startedAt = now.minusDays(60),
        endedAt = now.minusDays(1),
        expiredAt = LocalDateTime.now().plusDays(60)
      )
      passes.add(pass)
    }
    passRepository.saveAll(passes)
  }
}