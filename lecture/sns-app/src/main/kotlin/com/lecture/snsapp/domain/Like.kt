package com.lecture.snsapp.domain

import org.hibernate.annotations.GenericGenerator
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "tb_like")
class Like(
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

  @Column(name = "register_at")
  var registerAt: Timestamp? = null,

  @Column(name = "updated_at")
  var updatedAt: Timestamp? = null,

  @Column(name = "deleted_at")
  var deletedAt: Timestamp? = null,
) {
  companion object {
    fun of(user: User, post: Post): Like {
      return Like(user = user, post = post)
    }
  }
}