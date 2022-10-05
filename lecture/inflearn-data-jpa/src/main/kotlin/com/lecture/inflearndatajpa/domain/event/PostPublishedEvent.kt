package com.lecture.inflearndatajpa.domain.event

import com.lecture.inflearndatajpa.domain.Post
import org.springframework.context.ApplicationEvent

class PostPublishedEvent(
  val post: Post
): ApplicationEvent(post)