package com.toy.jpaeventlistener.domain

import com.toy.jpaeventlistener.domain.event.PostPublishedEvent
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.springframework.data.domain.AbstractAggregateRoot

@Entity
class Post(
  @Id
  @GeneratedValue
  var id: Long? = null,

  var title: String
): AbstractAggregateRoot<Post>() {

  fun publish(): Post {
    registerEvent(PostPublishedEvent(this))
    return this
  }
}