package com.toy.jpabasic.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Table
@Entity(name = "tb_role")
class Role (
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String,

  var name: String,

  @ElementCollection(targetClass = Authority::class, fetch = FetchType.EAGER)
  @JoinTable(name = "tb_join_role_authorities", joinColumns = [JoinColumn(name = "role_id")])
  @Column(name = "authority", nullable = false)
  @Enumerated(EnumType.STRING)
  val authorities: Set<Authority> = mutableSetOf()
)