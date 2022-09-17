package com.lecture.springbatchbasic.job

import com.lecture.springbatchbasic.core.domain.Member1VO
import com.lecture.springbatchbasic.core.domain.Member2VO
import com.lecture.springbatchbasic.core.domain.MemberFieldSetMapper
import com.lecture.springbatchbasic.core.service.MemberService
import org.slf4j.LoggerFactory
import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource

@Configuration
class FlatFileJobConfig(
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory
) {
  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun flatFileJob(flatFileStep: Step): Job =
    jbf.get("flatFileJob")
      .incrementer(RunIdIncrementer())
      .start(flatFileStep)
      .build()

  @JobScope
  @Bean
  fun flatFileStep(itemReader: FlatFileItemReader<Member1VO>, memberService: MemberService): Step =
    sbf.get("flatFileStep")
      .chunk<Member1VO, Member2VO>(5)
      .reader(itemReader)
      .processor(ItemProcessor<Member1VO, Member2VO> { memberService.classificationByAge(it) })
      .writer { it.forEach { member -> println(member) } }
      .build()

  @StepScope
  @Bean
  fun memberFlatFileItemReader(): FlatFileItemReader<Member1VO> =
    FlatFileItemReaderBuilder<Member1VO>()
      .name("memberFlatFileItemReader")
      .lineTokenizer(DelimitedLineTokenizer())
      .linesToSkip(1)
      .fieldSetMapper(MemberFieldSetMapper())
      .resource(ClassPathResource("books.csv"))
      .build()

}