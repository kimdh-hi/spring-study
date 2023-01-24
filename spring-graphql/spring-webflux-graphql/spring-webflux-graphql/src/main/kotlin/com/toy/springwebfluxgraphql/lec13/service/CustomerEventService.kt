package com.toy.springwebfluxgraphql.lec13.service

import com.toy.springwebfluxgraphql.lec13.vo.CustomerEvent
import org.springframework.stereotype.Service
import reactor.core.publisher.Sinks

@Service
class CustomerEventService {

  private val sink = Sinks.many().multicast().onBackpressureBuffer<CustomerEvent>()

  private val fluxSink = sink.asFlux().cache(0)

  fun emitEvent(event: CustomerEvent) {
    sink.tryEmitNext(event)
  }

  fun subscribe() = fluxSink
}