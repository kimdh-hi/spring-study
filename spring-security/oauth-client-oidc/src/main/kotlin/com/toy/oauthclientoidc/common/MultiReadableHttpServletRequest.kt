package com.toy.oauthclientoidc.common

import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import java.io.*
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

//@Component
class MultiReadableRequestBodyFilter : Filter {

  private val log = LoggerFactory.getLogger(javaClass)

  @Throws(IOException::class, ServletException::class)
  override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
    val wrappedRequest = MultiReadableRequestBodyHttpServletRequest(servletRequest as HttpServletRequest)
    val body = IOUtils.toString(wrappedRequest.reader)
    log.info(body)
    filterChain.doFilter(wrappedRequest, servletResponse)
  }
}

class MultiReadableRequestBodyHttpServletRequest(request: HttpServletRequest?) :
  HttpServletRequestWrapper(request) {
  private var cachedBytes: ByteArrayOutputStream? = null

  @Throws(IOException::class)
  override fun getInputStream(): ServletInputStream {
    if (cachedBytes == null) cacheInputStream()
    return CachedServletInputStream(cachedBytes!!.toByteArray())
  }

  @Throws(IOException::class)
  override fun getReader(): BufferedReader {
    return BufferedReader(InputStreamReader(inputStream))
  }

  @Throws(IOException::class)
  private fun cacheInputStream() {
    cachedBytes = ByteArrayOutputStream()
    IOUtils.copy(super.getInputStream(), cachedBytes)
  }

  class CachedServletInputStream(contents: ByteArray?) : ServletInputStream() {
    private val buffer: ByteArrayInputStream

    init {
      buffer = ByteArrayInputStream(contents)
    }

    override fun read(): Int {
      return buffer.read()
    }

    override fun isFinished(): Boolean {
      return buffer.available() == 0
    }

    override fun isReady(): Boolean {
      return true
    }

    override fun setReadListener(listener: ReadListener) {
      throw RuntimeException("Not implemented")
    }
  }
}