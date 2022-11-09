package com.toy.order.interfaces.partner

import com.toy.order.domain.partner.Partner
import com.toy.order.domain.partner.PartnerCommand
import com.toy.order.domain.partner.PartnerInfo
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PartnerRequestDto(
  @field:NotEmpty
  val partnerName: String,

  @field:NotEmpty
  @field:Email
  val email: String,

  @field:NotEmpty
  val businessNo: String
) {
  fun toCommand() =
    PartnerCommand(
      partnerName = partnerName,
      email = email,
      businessNo = businessNo
    )
}

data class PartnerResponseDto(
  val partnerToken: String,
  val partnerName: String,
  val businessNo: String,
  val email: String,
  val status: Partner.Status
) {
  companion object {
    fun of(partnerInfo: PartnerInfo) =
      with(partnerInfo) {
        PartnerResponseDto(
          partnerToken = partnerToken,
          partnerName = partnerName,
          businessNo = businessNo,
          email = email,
          status = status
        )
      }
  }
}