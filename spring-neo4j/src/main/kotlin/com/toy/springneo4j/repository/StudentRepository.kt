package com.toy.springneo4j.repository

import com.toy.springneo4j.domain.Student
import org.springframework.data.neo4j.repository.Neo4jRepository

interface StudentRepository: Neo4jRepository<Student, Long> {
}