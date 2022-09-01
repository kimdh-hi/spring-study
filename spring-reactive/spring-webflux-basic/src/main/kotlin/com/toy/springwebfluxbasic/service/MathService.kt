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
    return IntStream.rangeClosed(1, 9)
      .peek { Thread.sleep(1000L) }
      .peek { println("multiplication table: [$input * $it]") }
      .mapToObj { Response(output = input * it) }
      .collect(Collectors.toList())
  }
}