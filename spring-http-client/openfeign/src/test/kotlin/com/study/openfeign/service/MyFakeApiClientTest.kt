package com.study.openfeign.service

import com.study.openfeign.client.MyFakeApiClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MyFakeApiClientTest @Autowired constructor(
  private val myFakeApiClient: MyFakeApiClient,
) {


}
