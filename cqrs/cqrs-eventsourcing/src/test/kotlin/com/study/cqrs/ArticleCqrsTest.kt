package com.study.cqrs

import com.study.cqrs.application.command.ArticleCommandService
import com.study.cqrs.application.query.ArticleQueryService
import com.study.cqrs.domain.model.Article
import com.study.cqrs.infra.eventstore.EventStore
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class ArticleCqrsTest(
  @Autowired val command: ArticleCommandService,
  @Autowired val query: ArticleQueryService,
  @Autowired val eventStore: EventStore,
) {
  @Test
  fun `command 흐름이 이벤트로 읽기 모델에 반영된다`() {
    val articleId = command.writeArticle("user-1", "CQRS 입문", "본문")

    command.addComment(articleId, "댓글1")
    command.addComment(articleId, "댓글2")
    repeat(3) { command.increaseView(articleId) }

    val summary = query.get(articleId)
    assertEquals("user-1", summary.authorId)
    assertEquals("CQRS 입문", summary.title)
    assertEquals(2, summary.commentCount)
    assertEquals(3, summary.viewCount)
  }

  @Test
  fun `editArticle가 읽기 모델 제목을 갱신한다`() {
    val articleId = command.writeArticle("user-2", "원제목", "본문")

    command.editArticle(articleId, "새제목", "새본문")

    assertEquals("새제목", query.get(articleId).title)
  }

  @Test
  fun `이벤트 스트림을 재생하면 aggregate 상태가 복원된다`() {
    val articleId = command.writeArticle("user-3", "제목", "본문")
    command.editArticle(articleId, "수정제목", "수정본문")
    command.addComment(articleId, "댓글")
    command.increaseView(articleId)

    val events = eventStore.load(articleId)
    assertEquals(4, events.size)

    val rebuilt = Article.replay(events)
    assertEquals("수정제목", rebuilt.title)
    assertEquals("수정본문", rebuilt.content)
    assertEquals("user-3", rebuilt.authorId)
    assertEquals(1, rebuilt.commentCount)
    assertEquals(1, rebuilt.viewCount)
  }
}
