package com.toy.reactivejdsl.domain

import org.hibernate.annotations.GenericGenerator
import java.io.Serial
import javax.persistence.*

@Entity
@Table(name = "company")
class Company (
  @Id
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @GeneratedValue(generator = "uuid")
  var id: String? = null,
  var name: String,
): AbstractDateTraceEntity() {

  @OneToMany(mappedBy = "company", cascade = [CascadeType.REMOVE], orphanRemoval = true)
  val users: MutableSet<User> = mutableSetOf()

  companion object {
    @Serial
    private const val serialVersionUID: Long = 6604375143529578887L

    fun newCompany(name: String): Company = Company(name = name)
  }

  override fun getPk(): Any? = id

  override fun getType(): Any? = id!!::class

  override fun toString(): String {
    return "Company(id=$id, name='$name')"
  }
}