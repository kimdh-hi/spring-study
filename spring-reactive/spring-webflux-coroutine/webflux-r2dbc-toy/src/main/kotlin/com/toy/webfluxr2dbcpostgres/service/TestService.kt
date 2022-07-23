package com.toy.webfluxr2dbcpostgres.service

import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream

@Service
class TestService {
  fun callSyncJob() {
    val file = File("/Users/daehyunkim/Documents/study/spring-webflux-coroutine/webflux-r2dbc-postgres/test.txt")
    if (!file.exists()) {
      file.createNewFile()
    }

    FileOutputStream(file).use {
      it.write("test...".toByteArray())
    }
  }
}