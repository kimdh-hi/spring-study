package com.toy.springwebfluxgraphql.lec14.service

import com.toy.springwebfluxgraphql.lec14.repository.CustomerRepository
import com.toy.springwebfluxgraphql.lec14.vo.*
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CustomerService(
  private val customerRepository: CustomerRepository,
) {

  fun findAll(): Flux<CustomerVO> = customerRepository.findAll()
    .map { CustomerVO.of(it) }

  fun findById(id: Int): Mono<CustomerVO> = customerRepository.findById(id)
    .map { CustomerVO.of(it) }

  fun create(vo: CustomerVO): Mono<CustomerVO> = Mono.just(vo)
    .map { it.toEntity() }
    .flatMap { customerRepository.save(it) }
    .map { CustomerVO.of(it) }

  fun update(id: Int, vo: CustomerVO): Mono<CustomerVO> = customerRepository.findById(id)
    .map { vo.toEntity(it.id) }
    .flatMap { customerRepository.save(it) }
    .map { CustomerVO.of(it) }

  fun delete(id: Int) = customerRepository.deleteById(id)
    .thenReturn(CustomerDeleteResponseVO(id, CustomerDeleteStatus.SUCCESS))
    .onErrorReturn(CustomerDeleteResponseVO(id, CustomerDeleteStatus.FAILURE))
}