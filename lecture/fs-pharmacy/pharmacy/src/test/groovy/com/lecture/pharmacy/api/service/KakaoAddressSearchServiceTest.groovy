package com.lecture.pharmacy.api.service

import spock.lang.Specification

import java.nio.charset.StandardCharsets

class KakaoAddressSearchServiceTest extends Specification {

    private KakaoUriBuilderService kakaoUriBuilderService

    def setup() {
        kakaoUriBuilderService = new KakaoUriBuilderService()
    }

    def "buildUriByAddressSearch - 인코딩 성공"() {
        given:
        String address = "서울 동대문구"

        when:
        def uri = kakaoUriBuilderService.buildUriByAddress(address)
        def decodedUri = URLDecoder.decode(uri.toString(), StandardCharsets.UTF_8)

        then:
        decodedUri == "https://dapi.kakao.com/v2/local/search/address.json?query=서울 동대문구"
    }
}
