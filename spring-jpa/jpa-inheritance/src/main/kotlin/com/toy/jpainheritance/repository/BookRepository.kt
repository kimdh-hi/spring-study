package com.toy.jpainheritance.repository

import com.toy.jpainheritance.domain.Book
import org.springframework.data.repository.CrudRepository

interface BookRepository: CrudRepository<Book, Long>