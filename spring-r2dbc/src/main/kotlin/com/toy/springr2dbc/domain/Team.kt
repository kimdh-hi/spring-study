package com.toy.springr2dbc.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.relational.core.mapping.Table

@Table
@Entity(name = "tb_team")
class Team(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String = "",

  var name: String
) {

  @OneToMany(mappedBy = "team", cascade = [CascadeType.ALL], orphanRemoval = true)
  @JsonIgnore
  lateinit var users: List<User>
}