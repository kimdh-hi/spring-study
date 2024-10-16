package com.toy.springevent.domain

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "lt_mail_send_log")
class MailSendLog(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  var id: Long? = null,

  @Column
  @ColumnDefault("0")
  var success: Boolean = false
) {
  companion object {
    fun of(success: Boolean) = MailSendLog(
      success = success
    )
  }
}

interface MailSendLogRepository : JpaRepository<MailSendLog, Long>
