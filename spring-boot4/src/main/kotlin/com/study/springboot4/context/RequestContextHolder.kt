package com.study.springboot4.context

object RequestContextHolder {
  private val requestIdThreadLocal = ThreadLocal<String>()
  private val userInfoThreadLocal = ThreadLocal<String>()

  fun setRequestId(requestId: String) {
    requestIdThreadLocal.set(requestId)
  }

  fun getRequestId(): String? {
    return requestIdThreadLocal.get()
  }

  fun setUserInfo(userInfo: String) {
    userInfoThreadLocal.set(userInfo)
  }

  fun getUserInfo(): String? {
    return userInfoThreadLocal.get()
  }

  fun clear() {
    requestIdThreadLocal.remove()
    userInfoThreadLocal.remove()
  }
}
