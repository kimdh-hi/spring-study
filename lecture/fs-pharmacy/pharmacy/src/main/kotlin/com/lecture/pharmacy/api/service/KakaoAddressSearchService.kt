package com.lecture.pharmacy.api.service

import com.lecture.pharmacy.api.dto.KakaoResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class KakaoAddressSearchService(
  private val restTemplate: RestTemplate,
  private val kakaoUriBuilderService: KakaoUriBuilderService
) {

  @Value("\${kakao.rest.api.key}")
  lateinit var kakaoRestApiKey: String

  fun searchAddress(address: String): KakaoResponseDto? {
    val uri = kakaoUriBuilderService.buildUriByAddress(address)
    val httpHeaders = HttpHeaders()
    httpHeaders.set(HttpHeaders.AUTHORIZATION, kakaoRestApiKey)
    val httpEntity = HttpEntity<String>(httpHeaders)

    return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoResponseDto::class.java).body
  }
}