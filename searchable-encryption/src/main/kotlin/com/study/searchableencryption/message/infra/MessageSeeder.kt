package com.study.searchableencryption.message.infra

import com.study.searchableencryption.message.application.ChatMessageService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class MessageSeeder(
  private val chatMessageService: ChatMessageService,
) : ApplicationRunner {

  override fun run(args: ApplicationArguments) {
    chatMessageService.send(1, 1, "내일 회의 자료 공유 부탁드립니다")
    chatMessageService.send(1, 2, "회의실은 3층으로 예약했어요")
    chatMessageService.send(1, 1, "이번주 회의는 다음주 회의로 미룹니다")
    chatMessageService.send(1, 2, "계좌번호 110-234-567890 으로 보내주세요")
    chatMessageService.send(2, 3, "다른 방 메시지는 검색되지 않아야 한다")
  }
}
