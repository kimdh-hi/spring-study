package com.toy.springjpatsid.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.springjpatsid.domain.Space
import org.springframework.data.jpa.repository.JpaRepository

interface SpaceRepository: JpaRepository<Space, String>, SpaceRepositoryCustom

interface SpaceRepositoryCustom {

}

class SpaceRepositoryImpl(private val query: JPAQueryFactory): SpaceRepositoryCustom {

}