package com.toy.jpaeventlistener.repository

import com.toy.jpaeventlistener.domain.BSpace
import org.springframework.data.jpa.repository.JpaRepository

interface BSpaceRepository: JpaRepository<BSpace, String> {
}