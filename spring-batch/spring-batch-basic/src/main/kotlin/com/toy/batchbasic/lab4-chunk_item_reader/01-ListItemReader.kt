package com.toy.batchbasic.`lab4-chunk_item_reader`

import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ListItemReaderTestConfig(
  private val jbf: JobBuilderFactory,
  private val sbf: StepBuilderFactory
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun listItemReaderTestJob() = jbf.get("listItemReaderTestJob")
    .incrementer(RunIdIncrementer())
    .start(listItemReaderTestChunkBaseStep())
    .build()

  @Bean
  fun listItemReaderTestChunkBaseStep() = sbf.get("listItemReaderTestChunkBaseStep")
    .chunk<Member, Member>(3)
    .reader(CustomItemReader<Member>(ArrayList(getMembers())))
    .writer(itemWriter())
    .build()

  private fun itemWriter() = ItemWriter<Member> { members ->
    log.info(members
      .map { it.name }
      .joinToString(",")
    )
  }

  private fun getMembers(): MutableList<Member> {
    val members = mutableListOf<Member>()
    for (i in 0..10) {
      members.add(Member(i, "name$i"))
    }
    return members
  }
}

/*
ListItemReader 의 구현과 거의 동일
ArrayList 를 대상으로 removeAt은 성능상 굉장히 불리하다. LinkedList 를 사용하도록 하자.

ListItemReader 의 경우 LinkedList로 한 번 더 감싸서 작업하기 때문에 조금 낫다
 */
class CustomItemReader<T>(
  private val items: ArrayList<T>
): ItemReader<T> {

  override fun read(): T? {
    return if(items.isNotEmpty())
      items.removeAt(0)
    else
      null
  }
}

data class Member(
  val id: Int,
  val name: String
)