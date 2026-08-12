package com.toy.migration

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "tb_one_to_one_target")
class TargetEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,
)

@Entity
@Table(name = "tb_one_to_one_owner")
class OwnerEntity(
  @OneToOne
  @JoinColumn(name = "target_id")
  var target: TargetEntity,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,
)
