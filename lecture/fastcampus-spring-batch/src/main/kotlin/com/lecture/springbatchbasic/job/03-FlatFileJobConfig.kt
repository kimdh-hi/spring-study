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
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.adapter.ItemProcessorAdapter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor
import org.springframework.batch.item.file.transform.DelimitedLineAggregator
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import java.io.File

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
  fun flatFileStep(
    itemReader: FlatFileItemReader<Member1VO>,
    memberItemProcessorAdapter: ItemProcessorAdapter<Member1VO, Member2VO>,
    memberItemWriter: ItemWriter<Member2VO>
  ): Step =
    sbf.get("flatFileStep")
      .chunk<Member1VO, Member2VO>(5)
      .reader(itemReader)
//      .processor(ItemProcessor<Member1VO, Member2VO> { memberService.classificationByAge(it) })
      .processor(memberItemProcessorAdapter)
      .writer(memberItemWriter)
      .build()

  @StepScope
  @Bean
  fun memberItemWriter(): FlatFileItemWriter<Member2VO> {
    val fieldExtractor = BeanWrapperFieldExtractor<Member2VO>()
    fieldExtractor.setNames(arrayOf("id", "name", "age", "type"))
    fieldExtractor.afterPropertiesSet()

    val lineAggregator = DelimitedLineAggregator<Member2VO>()
    lineAggregator.setDelimiter("\t")
    lineAggregator.setFieldExtractor(fieldExtractor)

    File("processed-members.csv").createNewFile()
    val resource = FileSystemResource("processed-members.csv")

    return FlatFileItemWriterBuilder<Member2VO>()
      .name("memberFlatFileItemWriter")
      .resource(resource)
      .lineAggregator(lineAggregator)
      .build()
  }

  // ItemProcessorAdapter: processor 에서 service 로직 호출시 권장하는 방식
  @StepScope
  @Bean
  fun memberItemProcessorAdapter(memberService: MemberService): ItemProcessorAdapter<Member1VO, Member2VO> {
    val itemProcessorAdapter = ItemProcessorAdapter<Member1VO, Member2VO>()
    itemProcessorAdapter.setTargetObject(memberService)
    itemProcessorAdapter.setTargetMethod("classificationByAge")
    return itemProcessorAdapter
  }

  @StepScope
  @Bean
  fun memberFlatFileItemReader(): FlatFileItemReader<Member1VO> =
    FlatFileItemReaderBuilder<Member1VO>()
      .name("memberFlatFileItemReader")
      .lineTokenizer(DelimitedLineTokenizer())
      .linesToSkip(1)
      .fieldSetMapper(MemberFieldSetMapper())
      .resource(ClassPathResource("members.csv"))
      .build()

}