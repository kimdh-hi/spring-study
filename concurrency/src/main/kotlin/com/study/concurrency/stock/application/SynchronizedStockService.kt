package com.study.concurrency.stock.application

import com.study.concurrency.stock.domain.repository.StockRepository
import org.springframework.stereotype.Service

/**
 * JVM 모니터 락으로 임계 구역을 직렬화한다.
 *
 * 락은 반드시 트랜잭션 경계 **바깥**에서 잡아야 한다.
 * `@Transactional` 메서드에 직접 `synchronized` 를 걸면 프록시가 커밋을 락 해제 이후에 수행하므로,
 * 커밋 전 상태를 다음 스레드가 읽어 lost update 가 그대로 재현된다.
 *
 * 한계 — 단일 JVM 안에서만 유효하므로 인스턴스를 늘리면 정합성이 깨진다.
 */
@Service
class SynchronizedStockService(
  stockRepository: StockRepository,
  private val noLockStockService: NoLockStockService,
) : AbstractStockDecreaseService(stockRepository) {

  override val strategy = DecreaseStrategy.SYNCHRONIZED

  private val lock = Any()

  override fun decrease(stockId: Long, quantity: Long) {
    // 락 획득 → 트랜잭션 시작/커밋 → 락 해제 순서를 보장한다.
    synchronized(lock) {
      noLockStockService.decrease(stockId, quantity)
    }
  }
}
