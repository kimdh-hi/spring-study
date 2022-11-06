package com.lecture.fsmysql.domain.post

import com.lecture.fsmysql.domain.post.entity.Post
import com.lecture.fsmysql.domain.post.repository.PostRepository
import com.lecture.fsmysql.utils.PostFixtureFactory
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.util.StopWatch
import java.time.LocalDate
import java.util.stream.IntStream

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PostBulkInsertTest(
  private val postRepository: PostRepository
) {

  @Test
  @Disabled("post bulk insert")
  fun bulkInsert() {
    val easyRandom = PostFixtureFactory.get(
      1L,
      LocalDate.of(2020, 1, 1),
      LocalDate.of(2020, 2, 1)
    )

    val stopWatch = StopWatch()
    stopWatch.start()
    val posts = IntStream.range(0, 1_000_000)
      .parallel()
      .mapToObj { easyRandom.nextObject(Post::class.java) }
      .toList()
    stopWatch.stop()
    println("create posts time: ${stopWatch.totalTimeSeconds}")

    val queryStopWatch = StopWatch()
    queryStopWatch.start()
    postRepository.bulkInsert(posts)
    queryStopWatch.stop()
    println("posts bulk insert query time: ${queryStopWatch.totalTimeSeconds}")

  }
}