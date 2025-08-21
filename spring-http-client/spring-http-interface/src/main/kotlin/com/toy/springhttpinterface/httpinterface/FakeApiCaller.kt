package com.toy.springhttpinterface.httpinterface

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange

interface FakeApiCaller {

  @GetExchange("/Users/{userId}")
  fun getUsers(@PathVariable userId: String): Map<*, *>

}
