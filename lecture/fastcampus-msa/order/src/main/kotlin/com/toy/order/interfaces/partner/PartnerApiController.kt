package com.toy.order.interfaces.partner

import com.toy.order.application.partner.PartnerFacade
import com.toy.order.common.response.CommonResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/partners")
class PartnerApiController(
  private val partnerFacade: PartnerFacade
) {

  @PostMapping
  fun registerPartner(@RequestBody @Valid requestDto: PartnerRequestDto): CommonResponse<PartnerResponseDto> {
    val command = requestDto.toCommand()
    val partnerInfo = partnerFacade.registerPartner(command)
    val responseDto = PartnerResponseDto.of(partnerInfo)
    return CommonResponse.success(data = responseDto)
  }
}