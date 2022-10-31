package com.lecture.snsapp.domain

import org.hibernate.annotations.GenericGenerator
import java.sql.Timestamp
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "tb_comment")
class Comment(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String = "",

  @ManyToOne
  @JoinColumn(name = "user_id")
  val user: User,

  @ManyToOne
  @JoinColumn(name = "post_id")
  val post: Post,

  @Column(name = "comment")
  val comment: String,

  @Column(name = "register_at")
  var registerAt: Timestamp? = null,

  @Column(name = "updated_at")
  var updatedAt: Timestamp? = null,

  @Column(name = "deleted_at")
  var deletedAt: Timestamp? = null,
) {
  companion object {
    fun of(comment: String, user: User, post: Post): Comment {
      return Comment(user = user, post = post, comment = comment)
    }
  }

  @PrePersist
  fun prePersist() {
    this.registerAt = Timestamp.from(Instant.now())
  }
}