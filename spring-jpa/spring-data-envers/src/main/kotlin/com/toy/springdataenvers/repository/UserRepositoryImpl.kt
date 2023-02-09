package com.toy.springdataenvers.repository

import com.toy.springdataenvers.domain.User
import jakarta.persistence.EntityManager
import org.hibernate.envers.AuditReaderFactory
import org.hibernate.envers.query.AuditEntity
import java.sql.Timestamp
import java.time.LocalDateTime

class UserRepositoryImpl(
  private val entityManager: EntityManager,
): UserRepositoryCustom {

  override fun findRevisionsByIdAndCreatedDate(id: String, createdDate: LocalDateTime): List<User> {
    val query = AuditReaderFactory.get(entityManager).createQuery()
      .forRevisionsOfEntity(User::class.java, false, true)
      .add(AuditEntity.id().eq(id))
//      .addProjection(AuditEntity.property("id"))
//      .addProjection(AuditEntity.property("name"))
//      .addProjection(AuditEntity.property("age"))
//      .addProjection(AuditEntity.property("createdDate"))
      .add(AuditEntity.revisionProperty("timestamp").le(Timestamp.valueOf(createdDate).time))
      .addOrder(AuditEntity.revisionProperty("timestamp").desc())

    val result = mutableListOf<User>()
    val list: List<Array<Any>> = query.resultList as List<Array<Any>>
    list.forEach {
      val user = it[0] as User
      result.add(user)
    }

    return result
  }
}