package com.lecture.fsmysql.domain

import org.springframework.data.domain.Sort

object PageHelper {

  fun getSort(sort: Sort): String {
    if(sort.isEmpty)
      return "id DESC"
    return sort.toList()
      .map { "${it.property} ${it.direction}"  }
      .toList()
      .joinToString(", ")
  }
}