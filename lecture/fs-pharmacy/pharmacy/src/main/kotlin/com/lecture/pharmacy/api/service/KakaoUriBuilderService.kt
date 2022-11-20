package com.lecture.pharmacy.api.service

import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Service
class KakaoUriBuilderService {

  companion object {
    const val KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json"
  }

  fun buildUriByAddress(address: String): URI {
    return UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL)
      .queryParam("query", address)
      .build().encode().toUri()
  }
}