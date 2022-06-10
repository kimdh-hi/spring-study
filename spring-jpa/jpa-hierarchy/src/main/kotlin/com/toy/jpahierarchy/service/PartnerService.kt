package com.toy.jpahierarchy.service

import com.toy.jpahierarchy.domain.Partner
import com.toy.jpahierarchy.repository.PartnerRepository
import com.toy.jpahierarchy.vo.PartnerResponseVO
import com.toy.jpahierarchy.vo.PartnerSaveRequestVO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PartnerService(private val partnerRepository: PartnerRepository) {

  @Transactional
  fun save(requestVO: PartnerSaveRequestVO): Partner {
    val partner = requestVO.parentPartnerId?.let {
      val parentPartner = partnerRepository.findByIdOrNull(it) ?: throw IllegalArgumentException("data not found ...")
      Partner.newInstance(requestVO.name, parentPartner)
    } ?: run {
      Partner.newInstance(name = requestVO.name)
    }

    return partnerRepository.save(partner)
  }

  fun read(id: String): PartnerResponseVO {
    val partner = partnerRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("data not found ...")
    return PartnerResponseVO.fromEntity(partner)
  }

  fun readChildList(id: String): List<PartnerResponseVO> {
    val partner = partnerRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("data not found ...")
    return partner.childPartners.map {
      PartnerResponseVO.fromEntity(it)
    }.toList()
  }
}