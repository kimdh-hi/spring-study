package com.toy.springwebfluxgraphql.lec12.repository

import com.toy.springwebfluxgraphql.lec12.entity.Customer
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: ReactiveCrudRepository<Customer, Int>