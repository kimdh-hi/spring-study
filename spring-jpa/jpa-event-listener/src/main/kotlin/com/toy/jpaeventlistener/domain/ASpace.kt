package com.toy.jpaeventlistener.domain

import com.toy.jpaeventlistener.listener.ASpaceJpaListener
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "tb_a_space")
@EntityListeners(ASpaceJpaListener::class)
class ASpace(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var name: String,
)
