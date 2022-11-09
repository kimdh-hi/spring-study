package com.lecture.springbatchbasic.base

import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@ActiveProfiles("test")
@SpringBatchTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractBatchBaseTest {
  @Autowired
  protected lateinit var jobLauncherTestUtils: JobLauncherTestUtils
}