package com.study.gof.`03-행동`.`01-chain-of-responsibility`.`02-after`

fun main() {
  val requestHandler = AuthRequestHandler(
    LoggingRequestHandler(
      PrintRequestHandler(
        null
      )
    )
  )

  val client = Client(requestHandler)
  client.execute()
}

class Client(
  private val requestHandler: RequestHandler
) {

  fun execute() {
    val request = Request("test request...")
    requestHandler.handle(request)
  }
}

class PrintRequestHandler(
  nextHandler: RequestHandler? = null
): RequestHandler(nextHandler) {

  override fun handle(request: Request) {
    println("print...")
    super.handle(request)
  }
}

class LoggingRequestHandler(
  nextHandler: RequestHandler? = null
): RequestHandler(nextHandler) {

  override fun handle(request: Request) {
    println("logging...")
    super.handle(request)
  }
}

class AuthRequestHandler(
  nextHandler: RequestHandler? = null
): RequestHandler(nextHandler) {

  override fun handle(request: Request) {
    println("authentication..")
    super.handle(request)
  }
}

abstract class RequestHandler(
  private val nextHandler: RequestHandler? = null
) {

  open fun handle(request: Request) {
    nextHandler?.handle(request) ?: request.perform()
  }
}

data class Request(
  val data: String
) {
  fun perform() {
    println(data)
  }
}