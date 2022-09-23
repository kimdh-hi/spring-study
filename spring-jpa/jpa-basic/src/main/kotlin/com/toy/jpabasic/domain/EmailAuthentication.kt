package com.toy.jpabasic.domain

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serial
import java.io.Serializable
import javax.persistence.*

@Table
@Entity(name = "tb_email_authentication")
class EmailAuthentication (

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  @ManyToOne(optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_id")
  var user: User
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 2426676276920502136L
  }

}