package com.study.hexagonal.board.adapter.outbound.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface PostJpaRepository : JpaRepository<PostJpaEntity, Long>
