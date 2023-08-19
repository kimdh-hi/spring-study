package com.toy.jpaeventlistener.domain.event

import com.toy.jpaeventlistener.domain.Post
import org.springframework.context.ApplicationEvent

class PostPublishedEvent(
  private var post: Any
): ApplicationEvent(post) {

  init {
    if (post is Post) {
      post = post as Post
    }
  }

  fun getPost() = post
}