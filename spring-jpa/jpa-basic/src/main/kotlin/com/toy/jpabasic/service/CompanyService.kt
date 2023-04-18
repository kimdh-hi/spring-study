package com.toy.jpabasic.service

import com.toy.jpabasic.domain.Company
import com.toy.jpabasic.repository.CompanyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.DefaultTransactionDefinition

@Service
class CompanyService(
  private val companyRepository: CompanyRepository,
  private val transactionManager: PlatformTransactionManager
) {

  @Transactional
  fun delete(id: String) {
    companyRepository.findByIdOrNull(id)?.let {
      companyRepository.delete(it)
    }
  }

  fun test() {
    val tx = getTransaction()
    companyRepository.save(Company(name = "test"))
    transactionManager.commit(tx)
  }

  private fun getTransaction(): TransactionStatus {
    val transactionDefinition = DefaultTransactionDefinition().apply {
      propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
    }
    return transactionManager.getTransaction(transactionDefinition)
  }
}