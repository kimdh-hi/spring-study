package com.lecture.inflearndatajpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable
import javax.persistence.EntityManager

@NoRepositoryBean
interface GenericRepository<T: Any, ID: Serializable>: JpaRepository<T, ID> {
  fun contains(entity: T): Boolean
}

class SimpleGenericRepository<T: Any, ID: Serializable>(
  private val entityInformation: JpaEntityInformation<T, ID>,
  private val entityManager: EntityManager
): SimpleJpaRepository<T, ID>(entityInformation, entityManager),
  GenericRepository<T, ID> {

  override fun contains(entity: T): Boolean {
    return entityManager.contains(entity)
  }

}

