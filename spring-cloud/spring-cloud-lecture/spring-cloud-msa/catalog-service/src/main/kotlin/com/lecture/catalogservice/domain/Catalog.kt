package com.lecture.catalogservice.domain

import org.hibernate.annotations.GenericGenerator
import java.io.Serial
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "catalogs")
class Catalog(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var productId: String,

  var name: String,

  var stock: Int,

  var price: Int
): AbstractBaseEntity() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 7771538739541995991L
  }
}