package com.toy.jpainheritance.repository

import com.toy.jpainheritance.domain.Album
import org.springframework.data.repository.CrudRepository

interface AlbumRepository: CrudRepository<Album, Long>