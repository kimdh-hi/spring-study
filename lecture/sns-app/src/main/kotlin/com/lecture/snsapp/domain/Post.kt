package com.lecture.snsapp.domain

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "tb_post")
//@SQLDelete(sql = "update post set deleted_at = now() where id = ?")
//@Where(clause = "deleted_at is NULL")
class Post(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String = "",

  @Column(name = "title")
  var title: String,

  @Column(name = "body")
  var body: String,

  @ManyToOne
  @JoinColumn(name = "user_id")
  val user: User,

  @Column(name = "register_at")
  var registerAt: Timestamp? = null,

  @Column(name = "updated_at")
  var updatedAt: Timestamp? = null,

  @Column(name = "deleted_at")
  var deletedAt: Timestamp? = null,
) {
  companion object {
    fun of(title: String, body: String, user: User) = Post(
      title = title,
      body = body,
      user = user
    )
  }
}