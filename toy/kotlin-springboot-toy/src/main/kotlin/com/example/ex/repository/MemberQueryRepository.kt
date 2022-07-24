package com.example.ex.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class MemberQueryRepository(private val query: JPAQueryFactory, private val em: EntityManager) {

}