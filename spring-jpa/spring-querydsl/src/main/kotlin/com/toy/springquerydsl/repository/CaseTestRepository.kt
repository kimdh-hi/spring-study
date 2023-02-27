package com.toy.springquerydsl.repository

import com.toy.springquerydsl.domain.CaseTestEntity1
import com.toy.springquerydsl.domain.CaseTestEntity2
import com.toy.springquerydsl.domain.CaseTestEntity3
import org.springframework.data.jpa.repository.JpaRepository

interface CaseTestEntity1Repository: JpaRepository<CaseTestEntity1, Long> {
}

interface CaseTestEntity2Repository: JpaRepository<CaseTestEntity2, Long> {
}

interface CaseTestEntity3Repository: JpaRepository<CaseTestEntity3, Long> {

}