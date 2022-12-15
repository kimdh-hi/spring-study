package com.lecture.pharmacy.api.service

import com.lecture.pharmacy.api.AbstractIntegrationContainerBaseTest
import com.lecture.pharmacy.api.common.service.KakaoAddressSearchService
import org.springframework.beans.factory.annotation.Autowired

class KakaoAddressSearchServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private KakaoAddressSearchService addressSearchService

    def "address가 유효한 경우 document를 반환한다"() {
        given:
        def address = "서울 성구 종암로 10길"

        when:
        def result = addressSearchService.searchAddress(address)

        then:
        result.documents.size() > 0
        result.metaDto.totalCount > 0
        result.documents.get(0).addressName != null
    }
}
