package com.lecture.productservice.domain

import org.hibernate.annotations.GenericGenerator
import java.io.Serial
import javax.persistence.*

@Entity
@Table(name = "products")
class Product(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var name: String,

  var stock: Int,

  var price: Int
): AbstractBaseEntity() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 7771538739541995991L
  }
}