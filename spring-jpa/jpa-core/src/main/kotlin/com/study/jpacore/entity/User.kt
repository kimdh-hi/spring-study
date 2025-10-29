package com.study.jpacore.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.UuidGenerator

@Entity
class User private constructor(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var id: String? = null,

  @Column(length = 100, nullable = false)
  var name: String,
) : CreatedTimeAuditBaseEntity() {

//  @ManyToOne(fetch = FetchType.LAZY)
  @ManyToOne
  @JoinColumn(name = "user_id")
  var device: Device? = null

  companion object {
    fun of(name: String, device: Device? = null) = User(name = name).apply {
      this.device = device
    }
  }

  fun updateName(name: String) {
    this.name = name
  }
}
