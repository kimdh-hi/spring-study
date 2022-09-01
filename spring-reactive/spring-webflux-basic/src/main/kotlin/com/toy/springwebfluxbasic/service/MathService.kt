package com.toy.springwebfluxbasic.service

import com.toy.springwebfluxbasic.dto.Response
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import java.util.stream.IntStream

@Service
class MathService {

  fun getSquare(input: Int): Response {
    return Response(output = input * input)
  }

  fun multiplicationTable(input: Int): List<Response> {
    return IntStream.rangeClosed(1, 10)
      .peek { Thread.sleep(1000L) }
      .peek { println("math-service value: $it") }
      .mapToObj { Response(output = it * input) }
      .collect(Collectors.toList())
  }
}