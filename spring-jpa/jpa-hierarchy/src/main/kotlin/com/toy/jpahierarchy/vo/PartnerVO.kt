package com.toy.jpahierarchy.vo

import com.toy.jpahierarchy.domain.Partner

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

data class PartnerResponseVO(
  val id: String,
  val name: String,
  val parentId: String? = null
) {
  companion object {
    fun fromEntity(parent: Partner): PartnerResponseVO {
      parent.run {
        return PartnerResponseVO(id as String, name, parent.id)
      }
    }
  }
}