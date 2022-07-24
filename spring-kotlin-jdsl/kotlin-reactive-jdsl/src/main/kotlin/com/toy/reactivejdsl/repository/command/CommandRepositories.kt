package com.toy.reactivejdsl.repository.command

import com.toy.reactivejdsl.domain.Company
import com.toy.reactivejdsl.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String>

interface CompanyRepository: CrudRepository<Company, String>

