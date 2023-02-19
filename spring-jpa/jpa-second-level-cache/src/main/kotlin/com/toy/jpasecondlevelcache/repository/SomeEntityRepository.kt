package com.toy.jpasecondlevelcache.repository

import com.toy.jpasecondlevelcache.domain.SomeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SomeEntityRepository: JpaRepository<SomeEntity, Long>