package com.toy.redissondistributedlock.repository

import com.toy.redissondistributedlock.domain.Space
import com.toy.redissondistributedlock.domain.SpaceChannel
import org.springframework.data.repository.CrudRepository

interface SpaceRepository: CrudRepository<Space, String>

interface SpaceChannelRepository: CrudRepository<SpaceChannel, String>