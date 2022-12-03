package com.lecture.inflearnormjpa.hellojpa

import javax.persistence.Persistence

fun main() {
  val emf = Persistence.createEntityManagerFactory("hello")
  val em = emf.createEntityManager()

  val tx = em.transaction

  try {
    tx.begin()
//    val member = Member(id = 2L, name = "name")
//    em.persist(member)

    val member = em.find(Member::class.java, 1L)
    member.name = "update" // dirty-checking

    val findMember = em.createQuery("select m from Member m where m.id = :id", Member::class.java)
      .setParameter("id", 1L)
      .singleResult
    println("findMember: $findMember")

    tx.commit()
  } catch (e: Exception) {
    tx.rollback()
  } finally {
    em.close()
    emf.close()
  }
}