package com.lecture.inflearnormjpa.hellojpa

import javax.persistence.Persistence

fun main() {
  val emf = Persistence.createEntityManagerFactory("hello")
  val em = emf.createEntityManager()

  val tx = em.transaction

  try {
    tx.begin()

    tx.commit()
  } catch (e: Exception) {
    tx.rollback()
  } finally {
    em.close()
    emf.close()
  }
}