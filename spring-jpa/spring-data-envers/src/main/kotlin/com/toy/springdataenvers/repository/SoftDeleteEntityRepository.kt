package com.toy.springdataenvers.repository

import com.toy.springdataenvers.domain.SoftDeleteEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SoftDeleteEntityRepository: JpaRepository<SoftDeleteEntity, String>