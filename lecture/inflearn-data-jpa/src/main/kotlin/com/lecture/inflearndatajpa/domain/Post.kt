package com.lecture.inflearndatajpa.domain

import javax.persistence.*

@Entity
class Post(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  var title: String,

  @OneToMany(mappedBy = "post", cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
  val comments: MutableSet<Comment> = mutableSetOf()
) {
  fun addComment(comment: Comment) {
    comments.add(comment)
    comment.post = this
  }
}