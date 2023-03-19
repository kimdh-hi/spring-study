package com.toy.springneo4j.repository

import com.toy.springneo4j.domain.StudentClass
import org.springframework.data.neo4j.repository.Neo4jRepository

interface StudentClassRepository: Neo4jRepository<StudentClass, Long> {
}