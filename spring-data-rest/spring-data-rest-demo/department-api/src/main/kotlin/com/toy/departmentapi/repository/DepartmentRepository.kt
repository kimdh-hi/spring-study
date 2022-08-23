package com.toy.departmentapi.repository

import com.toy.departmentapi.domain.Department
import org.springframework.data.repository.CrudRepository

interface DepartmentRepository: CrudRepository<Department, Long>