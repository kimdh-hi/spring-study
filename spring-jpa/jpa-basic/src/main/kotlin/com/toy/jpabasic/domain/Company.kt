package com.toy.jpabasic.domain

import org.hibernate.annotations.GenericGenerator
import org.hibernate.envers.Audited
import org.hibernate.envers.RelationTargetAuditMode
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "tb_company")
@Audited
class Company (
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var name: String,

  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy = "company", cascade = [CascadeType.REMOVE])
  var users: MutableList<User> = mutableListOf()
) {

  override fun toString(): String {
    return "Company(id=$id, name='$name')"
  }
}