package com.toy.springexception.exception

import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.webmvc.error.ErrorAttributes
import org.springframework.boot.webmvc.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletWebRequest

@RestController
@RequestMapping("\${server.error.path:\${error.path:/error}}")
class GlobalErrorController(
  private val errorAttributes: ErrorAttributes,
) : ErrorController {

  @RequestMapping
  fun error(request: HttpServletRequest): ResponseEntity<ErrorResponse> {
    errorAttributes.getError(ServletWebRequest(request))?.let { return ErrorResponses.from(it) }

    val status = (request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) as? Int)
      ?.let { HttpStatus.resolve(it) }
      ?: HttpStatus.INTERNAL_SERVER_ERROR

    if (status == HttpStatus.NOT_FOUND) {
      return ErrorResponses.apiNotFound(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI)?.toString())
    }
    return ResponseEntity(ErrorResponse.of(ErrorCodes.UNKNOWN, status.reasonPhrase), status)
  }
}
