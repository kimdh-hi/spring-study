package com.toy.employeeapi.repository

import com.toy.employeeapi.domain.Employee
import org.springframework.data.repository.CrudRepository

interface EmployeeRepository: CrudRepository<Employee, Long>