package com.lecture.passbatch.job.pass

import com.lecture.passbatch.domain.enums.BulkPassStatus
import com.lecture.passbatch.repository.BulkPassRepository
import com.lecture.passbatch.repository.PassRepository
import com.lecture.passbatch.repository.UserGroupMappingRepository
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AddPassesTasklet(
  private val passRepository: PassRepository,
  private val bulkPassRepository: BulkPassRepository,
  private val userGroupMappingRepository: UserGroupMappingRepository
): Tasklet {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
    val startAt = LocalDateTime.now().minusDays(1)
    var count = 0

    bulkPassRepository.findByStatusAndStartedAtGratherThan(BulkPassStatus.READY, startAt)

    return RepeatStatus.FINISHED
  }
}