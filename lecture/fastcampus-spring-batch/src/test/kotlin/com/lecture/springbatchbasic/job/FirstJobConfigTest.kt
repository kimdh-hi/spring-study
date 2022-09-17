package com.lecture.springbatchbasic.job

import com.lecture.springbatchbasic.base.AbstractBatchBaseTest
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

@SpringBootTest(classes = [FirstJobConfig::class, BatchTestConfig::class])
internal class FirstJobConfigTest: AbstractBatchBaseTest() {

  @Test
  fun success() {
    //when
    val execution: JobExecution = jobLauncherTestUtils.launchJob()

    //then
    assertEquals(ExitStatus.COMPLETED, execution.exitStatus)
  }
}