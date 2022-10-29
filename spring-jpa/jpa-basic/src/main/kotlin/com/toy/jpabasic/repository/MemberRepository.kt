package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.Member
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface MemberRepository: CrudRepository<Member, String> {

  @Modifying
  @Query("update Member m set m.age = m.age + :age where m.age >= 20")
  fun bulkUpdateAddAge(age: Int): Int

  @Modifying(clearAutomatically = true)
  @Query("update Member m set m.age = m.age + :age where m.age >= 20")
  fun bulkUpdateAddAgeAutoClear(age: Int): Int

  fun findByName(name: String): Member?
}