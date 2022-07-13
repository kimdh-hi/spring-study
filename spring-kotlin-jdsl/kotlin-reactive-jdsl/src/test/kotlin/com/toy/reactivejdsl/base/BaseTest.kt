package com.toy.reactivejdsl.base

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.google.auto.service.AutoService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.TestConstructor
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import reactor.blockhound.BlockHound
import reactor.blockhound.integration.BlockHoundIntegration
import java.io.FilterInputStream

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class BaseTest {
  private val log = LoggerFactory.getLogger(javaClass)

  protected fun convertVO(vo: Any): MultiValueMap<String, String> {
    return try {
      val params: MultiValueMap<String, String> = LinkedMultiValueMap()

      val objectMapper = ObjectMapper()
      objectMapper.registerModule(JavaTimeModule())
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

      val map: Map<String, String> = objectMapper.convertValue(vo, object : TypeReference<Map<String, String>>() {})

      params.setAll(map)
      params
    } catch (e: Exception) {
      log.error("Parameter 변환중 오류가 발생했습니다. VO=[{}]", vo, e)
      throw IllegalStateException("Parameter 변환중 오류가 발생했습니다.")
    }
  }

  protected fun convertPageable(pageable: PageRequest?): MultiValueMap<String, String> {
    return try {
      val params: MultiValueMap<String, String> = LinkedMultiValueMap()
      if (pageable?.isPaged == true) {
        params["page"] = pageable.pageNumber.toString()
        params["size"] = pageable.pageSize.toString()

        if (pageable.sort.isSorted) {
          val sort = pageable.sort
          sort.forEach {
            println(it)
          }
        }
      }

      params
    } catch (e: Exception) {
      log.error("Parameter 변환중 오류가 발생했습니다.", e)
      throw IllegalStateException("Parameter 변환중 오류가 발생했습니다.")
    }
  }
}

@AutoService(BlockHoundIntegration::class)
class CustomBlockHoundIntegration: BlockHoundIntegration {

  override fun applyTo(builder: BlockHound.Builder) {
    builder
      .allowBlockingCallsInside("java.util.UUID", "randomUUID")
      .allowBlockingCallsInside("java.io.RandomAccessFile", "readBytes")
      .allowBlockingCallsInside(FilterInputStream::class.java.name, "read")
  }
}