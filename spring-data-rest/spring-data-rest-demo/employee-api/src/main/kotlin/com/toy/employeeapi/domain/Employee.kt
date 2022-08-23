package com.toy.employeeapi.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Employee(
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 var id: Long? = null,
 var name: String,
 var age: Int
) {

 fun getResourceId() = id // json 응답에 resourceId 추가
}