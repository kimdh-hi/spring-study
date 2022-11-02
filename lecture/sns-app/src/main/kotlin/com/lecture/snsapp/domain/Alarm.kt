package com.lecture.snsapp.domain

import com.lecture.snsapp.domain.enums.AlarmType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(
  name = "tb_alarm",
  indexes = [Index(name = "user_id_idx", columnList = "user_id")]
)
@TypeDef(name = "json", typeClass = JsonStringType::class)
class Alarm(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String = "",

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  val user: User, // 알람수신

  @ManyToOne
  @JoinColumn(name = "post_id")
  val post: Post,

  @Enumerated(EnumType.STRING)
  @Column(name = "alarm_type")
  val alarmType: AlarmType,

  @Type(type = "json")
  @Column(columnDefinition = "json")
  val alarmArgs: AlarmArgs,

  @Column(name = "register_at")
  var registerAt: Timestamp? = null,

  @Column(name = "updated_at")
  var updatedAt: Timestamp? = null,

  @Column(name = "deleted_at")
  var deletedAt: Timestamp? = null,
) {
  companion object {
    fun of(user: User, post: Post, alarmType: AlarmType, alarmArgs: AlarmArgs): Alarm {
      return Alarm(user = user, post = post, alarmType = alarmType, alarmArgs = alarmArgs)
    }
  }
}