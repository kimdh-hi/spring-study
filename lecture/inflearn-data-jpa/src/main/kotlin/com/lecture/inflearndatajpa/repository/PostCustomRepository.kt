package com.lecture.inflearndatajpa.repository

import com.lecture.inflearndatajpa.domain.Post
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.transaction.Transactional

interface PostCustomRepository<T> {
  fun findPost(): List<Post>

  fun delete(entity: T)
}

@Repository
@Transactional
class PostCustomRepositoryImpl(
  private val em: EntityManager
): PostCustomRepository<Post> {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun findPost(): List<Post>
    = em.createQuery("select p from Post p", Post::class.java)
      .resultList

  // 기존 jpaRepository 인터페이스와 중복
  // 기존 jpaRepository 인터페이스의 메서드를 오버라이딩한다.
  override fun delete(entity: Post) {
    log.info("PostCustomRepository.delete")
    em.remove(entity)
  }
}