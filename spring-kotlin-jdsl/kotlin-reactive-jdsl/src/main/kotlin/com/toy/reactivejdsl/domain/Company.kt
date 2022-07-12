package com.toy.reactivejdsl.domain

import org.hibernate.annotations.GenericGenerator
import java.io.Serial
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "company")
class Company (
  @Id
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @GeneratedValue(generator = "uuid")
  var id: String? = null,
  var name: String,
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 6604375143529578887L

    fun newCompany(name: String): Company = Company(name = name)
  }

  override fun toString(): String {
    return "Company(id=$id, name='$name')"
  }
}