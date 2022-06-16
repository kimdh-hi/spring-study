package com.toy.jpahierarchy.vo

import com.toy.jpahierarchy.domain.Partner
import javax.servlet.http.Part

data class PartnerSaveRequestVO (
  val name: String,
  val parentPartnerId: String? = null,
)

data class PartnerSaveResponseVO (
  val id: String,
  val name: String,
) {
  companion object {
    fun fromEntity(partner: Partner): PartnerSaveResponseVO {
      partner.run {
        return PartnerSaveResponseVO(id as String, name)
      }
    }
  }
}

data class PartnerDetailResponseVO(
  val id: String,
  val name: String,
  val parentId: String? = null
) {
  companion object {
    fun fromEntity(partner: Partner): PartnerDetailResponseVO {
      partner.run {
        return PartnerDetailResponseVO(id as String, name, partner.id)
      }
    }
  }
}

data class PartnerResponseVO(
  val id: String,
  val name: String,
) {
  companion object {
    fun fromEntity(partner: Partner): PartnerResponseVO {
      return PartnerResponseVO(partner.id!!, partner.name)
    }
  }
}

data class PartnerMeAndChildResponse(
  val id: String,
  val name: String,
  val childList: List<PartnerResponseVO>
) {
  companion object {
    fun fromEntity(partner: Partner): PartnerMeAndChildResponse {
      val childList = partner.childPartners.map {
        PartnerResponseVO.fromEntity(it)
      }.toList()

      return PartnerMeAndChildResponse(partner.id!!, partner.name, childList)
    }
  }
}