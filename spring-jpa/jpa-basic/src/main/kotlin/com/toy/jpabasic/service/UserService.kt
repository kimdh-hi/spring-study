package com.toy.jpabasic.service

import com.toy.jpabasic.domain.Company
import com.toy.jpabasic.domain.EmailAuthentication
import com.toy.jpabasic.domain.User
import com.toy.jpabasic.repository.CompanyRepository
import com.toy.jpabasic.repository.EmailAuthenticationRepository
import com.toy.jpabasic.repository.UserRepository
import com.toy.jpabasic.vo.UserSaveRequestVO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
@Transactional(readOnly = true)
class UserService(
  private val userRepository: UserRepository,
  private val companyRepository: CompanyRepository,
  private val emailAuthenticationService: EmailAuthenticationService,
): GenericService<User, String>(userRepository) {

  @PersistenceContext
  lateinit var em: EntityManager

//  @Transactional(rollbackFor = [IllegalArgumentException::class])
//  fun save(requestVO: UserSaveRequestVO): User {
//
//    val company = companyRepository.findByIdOrNull("comp-01")!!
//
//    val user = requestVO.toEntity(company)
//    val savedUser = userRepository.save(user)
//    val emailAuthentication = EmailAuthentication(user = savedUser)
//    emailAuthenticationService.save(emailAuthentication)
//    if (savedUser.username == "error") {
//      throw IllegalArgumentException("error ...")
//    }
//
//    return savedUser
//  }

  @Transactional
  fun authentication(id: String) {
    val emailAuthentication =
      emailAuthenticationService.get(id)
    val user = emailAuthentication.user
    user.username = "updateUsername"
  }

  @Transactional
  fun authenticationV2(id: String) {

    val emailAuthentication = emailAuthenticationService.get(id)
    val user = emailAuthentication.user
    println("before: ${em.contains(user)}")

    user.username = "updateUsername"
  }
}