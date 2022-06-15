package com.toy.springcoroutineexample.service

import com.toy.springcoroutineexample.vo.ConcatRequest
import org.springframework.stereotype.Service

@Service
class ConcatService {

  fun concate(concatRequest: ConcatRequest): String {
    return concatRequest.list.joinToString(",")
  }
}