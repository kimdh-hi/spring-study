package com.lecture.springbatchbasic.core.service

import com.lecture.springbatchbasic.core.domain.Member1VO
import com.lecture.springbatchbasic.core.domain.Member2VO
import org.springframework.stereotype.Service

@Service
class MemberService {

  fun classificationByAge(member1VO: Member1VO): Member2VO {
    return if(member1VO.age < 20) {
      Member2VO(id = member1VO.id, member1VO.name, member1VO.age, type = "child")
    } else {
      Member2VO(id = member1VO.id, member1VO.name, member1VO.age, type = "adult")
    }
  }
}