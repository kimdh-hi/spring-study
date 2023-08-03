package com.toy.jpaeventlistener.repository

import com.toy.jpaeventlistener.domain.ASpace
import org.springframework.data.jpa.repository.JpaRepository

interface ASpaceRepository: JpaRepository<ASpace, String> {
}