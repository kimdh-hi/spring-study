package com.toy.springneo4j.repository

import com.toy.springneo4j.domain.Subject
import org.springframework.data.neo4j.repository.Neo4jRepository

interface SubjectRepository: Neo4jRepository<Subject, Long> {
}