package com.lecture.springbatchbasic.job

import com.lecture.springbatchbasic.base.BatchTestConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [FirstJobConfig::class, BatchTestConfig::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class FirstJobConfigTest(
  private val jobLauncherTestUtils: JobLauncherTestUtils
) {

  @Test
  fun success() {
    //when
    val execution: JobExecution = jobLauncherTestUtils.launchJob()

    //then
    assertEquals(ExitStatus.COMPLETED, execution.exitStatus)
  }
}