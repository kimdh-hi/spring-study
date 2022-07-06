package com.toy.springcoroutineexample.service

import com.toy.springcoroutineexample.vo.ConcatenatedRequestResult
import org.springframework.stereotype.Service

@Service
class ConcatService {

  fun concate(concatenatedRequestResult: ConcatenatedRequestResult): String {
    return concatenatedRequestResult.list.joinToString(",")
  }
}