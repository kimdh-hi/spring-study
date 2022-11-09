package com.lecture.springbatchbasic.job

import com.lecture.springbatchbasic.base.AbstractBatchBaseTest
import com.lecture.springbatchbasic.base.BatchTestConfig
import com.lecture.springbatchbasic.core.service.MemberService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.test.AssertFile
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.FileSystemResource

@SpringBootTest(classes = [FlatFileJobConfig::class, MemberService::class, BatchTestConfig::class])
internal class FlatFileJobConfigTest: AbstractBatchBaseTest() {

  @Test
  fun `success`() {
    //when
    val result = jobLauncherTestUtils.launchJob()

    //then
    assertAll({
      assertEquals(ExitStatus.COMPLETED, result.exitStatus)
      AssertFile.assertFileEquals(
        FileSystemResource("processed-members.csv"), FileSystemResource("test-success-processed-members.csv")
      )
    })
  }
}