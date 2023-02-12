package com.toy.jpainheritance.repository

import com.toy.jpainheritance.domain.Movie
import org.springframework.data.repository.CrudRepository

interface MovieRepository: CrudRepository<Movie, Long> {
}