package com.lecture.inflearndatajpa.domain

import javax.persistence.*

@Entity
class Comment(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  var comment: String,

  @ManyToOne
  @JoinColumn(name = "post_id")
  var post: Post? = null
)