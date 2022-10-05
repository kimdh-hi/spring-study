package com.lecture.inflearndatajpa.domain

import com.lecture.inflearndatajpa.domain.event.PostPublishedEvent
import org.springframework.data.domain.AbstractAggregateRoot
import javax.persistence.*

@Entity
class Post(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  var title: String,

  @OneToMany(mappedBy = "post", cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
  val comments: MutableSet<Comment> = mutableSetOf()
): AbstractAggregateRoot<Post>() {
  fun addComment(comment: Comment) {
    comments.add(comment)
    comment.post = this
  }

  override fun toString(): String {
    return "Post(id=$id, title='$title', comments=$comments)"
  }

  fun publish(): Post {
    this.registerEvent(PostPublishedEvent(this))
    return this
  }
}