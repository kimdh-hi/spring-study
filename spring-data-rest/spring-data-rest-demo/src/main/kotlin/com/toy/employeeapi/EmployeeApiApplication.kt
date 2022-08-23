package com.toy.employeeapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EmployeeApiApplication

fun main(args: Array<String>) {
  runApplication<EmployeeApiApplication>(*args)
}
