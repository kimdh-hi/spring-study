package com.lecture.inflearndatajpa.domain

import javax.persistence.Embeddable

@Embeddable
class Address(
  var street: String,
  var city: String,
  var state: String,
  var zipCode: String
)