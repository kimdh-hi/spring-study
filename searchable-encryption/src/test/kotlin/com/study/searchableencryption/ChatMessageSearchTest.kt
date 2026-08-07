package com.study.searchableencryption

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.study.searchableencryption.message.application.ChatMessageService
import com.study.searchableencryption.message.domain.model.QChatMessage.Companion.chatMessage
import com.study.searchableencryption.message.domain.model.QMessageToken.Companion.messageToken
import com.study.searchableencryption.message.domain.repository.ChatMessageRepository
import com.study.searchableencryption.message.domain.repository.MessageTokenRepository
import com.study.searchableencryption.message.infra.BlindIndex
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class ChatMessageSearchTest {

  @Autowired
  private lateinit var chatMessageService: ChatMessageService

  @Autowired
  private lateinit var chatMessageRepository: ChatMessageRepository

  @Autowired
  private lateinit var messageTokenRepository: MessageTokenRepository

  @Autowired
  private lateinit var chatMessageSearchRepository: ChatMessageSearchRepository

  @Autowired
  private lateinit var blindIndex: BlindIndex

  @Autowired
  private lateinit var query: JPAQueryFactory

  @BeforeEach
  fun setUp() {
    messageTokenRepository.deleteAll()
    chatMessageRepository.deleteAll()

    chatMessageService.send(ROOM, 1, "내일 회의 자료 공유 부탁드립니다")
    chatMessageService.send(ROOM, 2, "회의실은 3층으로 예약했어요")
    chatMessageService.send(ROOM, 1, "이번주 회의는 다음주 회의로 미룹니다")
    chatMessageService.send(OTHER_ROOM, 3, "다른 방 회의 자료입니다")
  }

  @Test
  fun `같은 내용을 두 번 저장하면 암호문이 서로 다르다`() {
    val first = chatMessageService.send(ROOM, 1, "동일한 메시지")
    val second = chatMessageService.send(ROOM, 1, "동일한 메시지")

    assertNotEquals(cipherOf(first.id!!), cipherOf(second.id!!))
    assertEquals(first.content, second.content)
  }

  @Test
  fun `DB 컬럼을 직접 읽으면 평문이 없다`() {
    val ciphers = query.select(RAW_CONTENT).from(chatMessage).fetch()

    assertTrue(ciphers.isNotEmpty())
    assertTrue(ciphers.none { it.contains("회의") })
    assertTrue(ciphers.all { it.matches(Regex("[0-9a-f]+")) })
  }

  @Test
  fun `암호문 컬럼에 LIKE 검색은 아무것도 찾지 못한다`() {
    assertEquals(0, chatMessageService.searchByLikeOnCipher(ROOM, "회의").size)
  }

  @Test
  fun `n-gram 토큰으로 중간 일치 검색이 된다`() {
    val contents = chatMessageService.search(ROOM, "회의 자료").map { it.content }

    assertEquals(1, contents.size)
    assertContains(contents.first(), "회의 자료")
  }

  @Test
  fun `토큰은 모두 존재하지만 부분문자열이 아닌 오탐은 복호화 재검증으로 걸러진다`() {
    val matchedIds = chatMessageSearchRepository.findIdsMatchingAllTokens(ROOM, blindIndex.tokens("이번주 회의로"))

    assertEquals(1, matchedIds.size)
    assertTrue(chatMessageService.search(ROOM, "이번주 회의로").isEmpty())
  }

  @Test
  fun `토큰 검색 결과는 전건 복호화 결과와 같다`() {
    listOf("회의", "회의 자료", "다음주", "3층").forEach { query ->
      assertEquals(
        chatMessageService.searchByFullScan(ROOM, query).map { it.content },
        chatMessageService.search(ROOM, query).map { it.content },
        "query=$query",
      )
    }
  }

  @Test
  fun `다른 방 메시지는 검색되지 않는다`() {
    val contents = chatMessageService.search(ROOM, "회의 자료").map { it.content }

    assertFalse(contents.any { it.contains("다른 방") })
    assertEquals(1, chatMessageService.search(OTHER_ROOM, "회의 자료").size)
  }

  @Test
  fun `메시지를 삭제하면 검색 토큰도 함께 사라진다`() {
    val message = chatMessageService.send(ROOM, 1, "삭제될 메시지입니다")
    assertEquals(1, chatMessageService.search(ROOM, "삭제될").size)

    chatMessageService.delete(message.id!!)

    assertTrue(chatMessageService.search(ROOM, "삭제될").isEmpty())
    assertEquals(0, tokenCountOf(message.id!!))
  }

  private fun tokenCountOf(messageId: Long): Long =
    query.select(messageToken.count())
      .from(messageToken)
      .where(messageToken.messageId.eq(messageId))
      .fetchOne()!!

  private fun cipherOf(id: Long): String =
    query.select(RAW_CONTENT)
      .from(chatMessage)
      .where(chatMessage.id.eq(id))
      .fetchOne()!!

  companion object {
    private const val ROOM = 1L
    private const val OTHER_ROOM = 2L
    private val RAW_CONTENT = Expressions.stringTemplate("cast({0} as string)", chatMessage.content)
  }
}
