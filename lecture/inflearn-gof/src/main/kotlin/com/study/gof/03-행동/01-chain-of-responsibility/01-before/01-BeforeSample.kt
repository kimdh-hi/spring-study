package com.study.gof.`03-행동`.`01-chain-of-responsibility`.`01-before`

/*
handler 에 추가적이 요구사항이 생기는 경우 SRP 를 위배하지 않도록 상속을 통해 해결
클라이언트는 구체적으로 어떤 handler 를 사용할지 알아야 함

인증도, 로깅도 모두 필요하다면??
 */

fun main() {
  val request = Request("test")
//  val requestHandler = RequestHandler()
//  val requestHandler = AuthRequestHandler()
  val requestHandler = LoggingRequestHandler()
  requestHandler.handle(request)
}

open class RequestHandler {

  open fun handle(request: Request) {
    println(request.perform())
  }
}

class AuthRequestHandler: RequestHandler() {

  override fun handle(request: Request) {
    println("authentication...")
    super.handle(request)
  }
}

class LoggingRequestHandler: RequestHandler() {

  override fun handle(request: Request) {
    println("logging...")
    super.handle(request)
  }
}

data class Request(
  val data: String
) {
  fun perform(): String {
    return data
  }
}