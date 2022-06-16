package com.toy.jpahierarchy.controller

import com.toy.jpahierarchy.service.PartnerService
import com.toy.jpahierarchy.vo.PartnerResponseVO
import com.toy.jpahierarchy.vo.PartnerSaveRequestVO
import com.toy.jpahierarchy.vo.PartnerSaveResponseVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/partners")
class PartnerController(
  private val partnerService: PartnerService
) {

  @PostMapping
  fun save(@RequestBody requestVO: PartnerSaveRequestVO): ResponseEntity<PartnerSaveResponseVO> {
    val partner = partnerService.save(requestVO)

    return ResponseEntity.ok(PartnerSaveResponseVO.fromEntity(partner))
  }

  @GetMapping
  fun list() = ResponseEntity.ok(partnerService.list())

  @GetMapping("/{id}/child")
  fun meAndChild(@PathVariable id: String) = ResponseEntity.ok(partnerService.getMeAndChildPartners(id))


  @GetMapping("/{id}")
  fun read(@PathVariable id: String) = ResponseEntity.ok(partnerService.read(id))

  @GetMapping("/v2/{id}")
  fun readV2(@PathVariable id: String) = ResponseEntity.ok(partnerService.readV2(id))

}