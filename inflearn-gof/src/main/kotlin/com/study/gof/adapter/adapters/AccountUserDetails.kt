package com.study.gof.adapter.adapters

import com.study.gof.adapter.Account
import com.study.gof.adapter.security.UserDetails

// 별도의 클래스가 아닌 어댑터를 필요로 하는 클래스에서 타켓 인터페이스를 구현하는 방식으로도 어댑터 패턴 적용 가능.
// Spring Security 에서 CustomUserDetails 을 만들거나 User에서 UserDetails 를 구현하거나의 차이...
class AccountUserDetails(
  private val account: Account
): UserDetails {

  override fun getUsername() = account.username

  override fun getPassword() = account.password
}