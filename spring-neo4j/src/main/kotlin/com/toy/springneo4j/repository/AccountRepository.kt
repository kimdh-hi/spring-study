package com.toy.springneo4j.repository

import com.toy.springneo4j.domain.Account
import org.springframework.data.neo4j.repository.Neo4jRepository

interface AccountRepository:  Neo4jRepository<Account, Long> {

}