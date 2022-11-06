package com.lecture.fsmysql.utils

import com.lecture.fsmysql.domain.post.entity.Post
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates.*
import java.time.LocalDate

object PostFixtureFactory {

  fun get(memberId: Long, firstDate: LocalDate, lastDate: LocalDate): EasyRandom {
    val idField = named(Post::id.name)
      .and(ofType(Long::class.java))
      .and(inClass(Post::class.java))

    val memberIdField = named(Post::memberId.name)
      .and(ofType(Long::class.java))
      .and(inClass(Post::class.java))

    val param = EasyRandomParameters()
      .excludeField(idField)
      .randomize(memberIdField) { memberId }
      .dateRange(firstDate, lastDate)

    return EasyRandom(param)
  }
}