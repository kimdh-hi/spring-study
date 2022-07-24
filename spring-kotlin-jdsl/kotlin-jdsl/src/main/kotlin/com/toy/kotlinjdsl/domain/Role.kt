package com.toy.kotlinjdsl.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity(name = "tb_role")
@Table
class Role(
  @Id
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @GeneratedValue(generator = "uuid")
  var id: String,
  var name: String,

  @ElementCollection(targetClass = Authority::class, fetch = FetchType.LAZY)
  @JoinTable(name ="tb_role_authorities", joinColumns = [JoinColumn(name = "role_id")])
  @Column(name = "authority", nullable = false)
  @Enumerated(EnumType.STRING)
  val authorities: Set<Authority> = mutableSetOf()
)