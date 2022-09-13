package com.toy.order.domain.partner

// 새부 구현체는 infrastructure 계층으로 내린다.
// domain 계층에서는 최대한 높은 추상화를 목표로 한다
// db 접근 기술에 대한 구현체는 infrastructure 계층에서 jpa, jdbcTemplate, queryDSL 등 적합한 것으로 골라 쓰고
// db 접근 기술이 바뀐다고 해도 구현체만 갈아 끼워지도록 한다.
interface PartnerStore {
  fun store(partner: Partner): Partner
}
