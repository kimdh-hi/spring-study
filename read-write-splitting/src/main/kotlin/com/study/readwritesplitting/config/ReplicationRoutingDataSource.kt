package com.study.readwritesplitting.config

import org.slf4j.LoggerFactory
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

class ReplicationRoutingDataSource : AbstractRoutingDataSource() {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun determineCurrentLookupKey(): Any {
    val readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly()
    return if (readOnly) {
      log.info("Routing -> READ (replica)")
      RoutingDataSourceType.READ
    } else {
      log.info("Routing -> WRITE (master)")
      RoutingDataSourceType.WRITE
    }
  }
}
