package com.study.concurrency.stock.application

enum class DecreaseStrategy {
  /** 락 없음. 동시 요청 시 lost update 발생. */
  NO_LOCK,

  /** JVM 모니터 락. 단일 인스턴스에서만 유효. */
  SYNCHRONIZED,

  /** @Version 기반 낙관적 락 + 재시도. */
  OPTIMISTIC,

  /** SELECT ... FOR UPDATE 기반 비관적 락. */
  PESSIMISTIC,

  /** DB 조건부 원자적 UPDATE. */
  ATOMIC_UPDATE,
}
