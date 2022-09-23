package com.lecture.issueservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IssueServiceApplication

fun main(args: Array<String>) {
  runApplication<IssueServiceApplication>(*args)
}
