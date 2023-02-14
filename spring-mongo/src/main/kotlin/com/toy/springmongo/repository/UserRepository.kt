package com.toy.springmongo.repository

import com.toy.springmongo.domain.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String>